package com.mm.kim.mentormentee.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mm.kim.mentormentee.common.code.Config;
import com.mm.kim.mentormentee.common.code.ErrorCode;
import com.mm.kim.mentormentee.common.exception.HandlableException;
import com.mm.kim.mentormentee.common.mail.EmailSender;
import com.mm.kim.mentormentee.common.util.file.FileInfo;
import com.mm.kim.mentormentee.common.util.file.FileUtil;
import com.mm.kim.mentormentee.member.validator.JoinForm;
import com.mm.kim.mentormentee.member.validator.ModifyPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MenteeRepository menteeRepository;
    private final MentorRepository mentorRepository;
    private final EmailSender emailSender;
    private final RestTemplate http;
    private final PasswordEncoder passwordEncoder;

    public Member authenticateUser(Member member) {
        Member memberEntity = memberRepository.findById(member.getUserId()).orElse(null);

        if (memberEntity == null) return null;
        if (!passwordEncoder.matches(member.getPassword(), memberEntity.getPassword())) return null;

        return memberEntity;
    }

    public boolean existsMemberById(String userId) {
        return memberRepository.existsById(userId);
    }

    public void authenticateByEmail(JoinForm form, String token, String template, String subject) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("mailTemplate", template);
        body.add("userId", form.getUserId());
        body.add("persistToken", token);

        RequestEntity<MultiValueMap<String, String>> request =
                RequestEntity.post(Config.DOMAIN.DESC + "/mail")
                        .accept(MediaType.APPLICATION_FORM_URLENCODED)
                        .body(body);

        String htmlText = http.exchange(request, String.class).getBody();
        emailSender.send(form.getEmail(), subject, htmlText);
    }

    public void persistMentor(Mentor mentor) {
        mentor.getMember().setPassword(passwordEncoder.encode(mentor.getMember().getPassword()));
        FileInfo qrInfo = new FileInfo();

        String link = createQrLink(mentor);
        qrInfo = createQRImage(link, parseRGBStringToInt("#343a40"), 0xFFFFFFFF);

        mentor.setQrInfo(qrInfo);
        mentorRepository.save(mentor);
    }

    private int parseRGBStringToInt(String color) {
        color = color.substring(1);
        color = "ff" + color;
        long l = Long.parseLong(color, 16);
        return (int)l;
    }

    private FileInfo createQRImage(String link, int qrColor, int qrBgColor) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        FileInfo fileinfo = new FileInfo();
        try {
            //qr 생성
            BitMatrix bitMatrix = qrCodeWriter.encode(link, BarcodeFormat.QR_CODE, 300, 300);
            MatrixToImageConfig config = new MatrixToImageConfig(qrColor, qrBgColor); //qr코드 색지정

            BufferedImage qrimage = MatrixToImageWriter.toBufferedImage(bitMatrix, config);

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int date = cal.get(Calendar.DAY_OF_MONTH);
            String subPath = year + "/" + month + "/" + date + "/";
            String uploadPath = Config.UPLOAD_PATH.DESC + subPath;

            File dir = new File(uploadPath);
            if(!dir.exists()) {
                dir.mkdirs();
            }

            String renameFileName = UUID.randomUUID().toString();
            fileinfo.setRenameFileName(renameFileName);
            fileinfo.setSavePath(subPath);
            File qrImage = new File( uploadPath + renameFileName + ".png");

            //경로에 이미지 생성
            ImageIO.write(qrimage, "png", qrImage);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        return fileinfo;
    }

    private String createQrLink(Mentor mentor) {
        URL url = null;
        URLConnection connection = null;
        StringBuilder responseBody = new StringBuilder();
        String link = null;

        try {
            url = new URL("https://toss.im/transfer-web/linkgen-api/link");
            connection = url.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> commandMap = Map.of("apiKey", "dcf102a946024eafb1c3d61cbdba3c47"
                    , "bankName", mentor.getBank()
                    , "bankAccountNo", mentor.getAccountNum()
                    , "amount", "10000"
                    , "message", "토스 입금");
            String asString = mapper.writeValueAsString(commandMap);

            BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
            bos.write(asString.getBytes(StandardCharsets.UTF_8));
            bos.flush();
            bos.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            while ((line = br.readLine()) != null) {
                responseBody.append(line);
            }
            br.close();
        } catch (Exception e) {
            responseBody.append(e);
        }
        String resStr = responseBody.toString();
        link = resStr.substring(resStr.indexOf("https"), resStr.lastIndexOf("\""));
        return link;
    }

    public void persistMentee(Mentee mentee) {
        mentee.getMember().setPassword(passwordEncoder.encode(mentee.getMember().getPassword()));
        menteeRepository.save(mentee);
    }

    public Mentor findMentorByMember(Member certifiedMember) {
        return mentorRepository.findMentorByMember(certifiedMember);
    }

    public Mentee findMenteeByMember(Member certifiedMember) {
        return menteeRepository.findMenteeByMember(certifiedMember);
    }

    @Transactional
    public void modifyPassword(ModifyPassword modifyPassword) {
        Member member = memberRepository.findMemberByUserId(modifyPassword.getUserId());

        member.setPassword(passwordEncoder.encode(modifyPassword.getNewPw()));
    }

    @Transactional
    public Mentor modifyMentor(Mentor mentor) {
        Mentor curMentor = mentorRepository.findByMentorIdx(mentor.getMentorIdx());
        curMentor.getMember().setUserName(mentor.getMember().getUserName());
        curMentor.getMember().setEmail(mentor.getMember().getEmail());
        curMentor.getMember().setPhone(mentor.getMember().getPhone());
        curMentor.getMember().setAddress(mentor.getMember().getAddress());
        curMentor.setUniversityName(mentor.getUniversityName());
        curMentor.setUniversityType(mentor.getUniversityType());
        curMentor.setGrade(mentor.getGrade());
        curMentor.setWantTime(mentor.getWantTime());
        curMentor.setWantDay(mentor.getWantDay());
        if (mentor.getHistory() != null) {
            curMentor.setHistory(curMentor.getHistory() + "," + mentor.getHistory());
        }

        return curMentor;
    }

    @Transactional
    public Mentee modifyMentee(Mentee mentee) {
        Mentee curMentee = menteeRepository.findByMenteeIdx(mentee.getMenteeIdx());
        curMentee.getMember().setUserName(mentee.getMember().getUserName());
        curMentee.getMember().setEmail(mentee.getMember().getEmail());
        curMentee.getMember().setPhone(mentee.getMember().getPhone());
        curMentee.getMember().setAddress(mentee.getMember().getAddress());
        curMentee.setGrade(mentee.getGrade());
        curMentee.setHopeMajor(mentee.getHopeMajor());
        curMentee.setMajor(mentee.getMajor());
        curMentee.setHopeUniversity(mentee.getHopeUniversity());
        curMentee.setSchoolName(mentee.getSchoolName());
        return curMentee;
    }

    @Transactional
    public Mentor modifyAccount(Mentor mentor, String bank, String accountNum) {
        Mentor curMentor = mentorRepository.findByMentorIdx(mentor.getMentorIdx());
        curMentor.setBank(mentor.getBank());
        curMentor.setAccountNum(mentor.getAccountNum());
        String link = createQrLink(curMentor);
        FileInfo qrInfo = createQRImage(link, parseRGBStringToInt("#343a40"), 0xFFFFFFFF);
        curMentor.setQrInfo(qrInfo);
        return curMentor;
    }

    public Member findMemberByKaKaoId(String id) {
        return memberRepository.findMemberByKakaoJoin(id).orElse(null);
    }

    @Transactional
    public void modifyMemberKakaoJoin(Member member) {
        Member curMember = memberRepository.findById(member.getUserId()).orElseThrow(() ->
                new HandlableException(ErrorCode.VALIDATOR_FAIL_ERROR));
        curMember.setKakaoJoin(member.getKakaoJoin());
    }

    @Transactional
    public Mentor modifyMentorImage(MultipartFile image, Mentor mentor) {
        Mentor curMentor =  mentorRepository.findByMentorIdx(mentor.getMentorIdx());

        FileInfo fileInfo = new FileInfo();
        FileUtil fileUtil = new FileUtil();

        fileInfo = fileUtil.fileUpload(image);

        curMentor.setFileInfo(fileInfo);

        return curMentor;
    }

    public boolean deleteMember(HttpSession session, String role, String password) {
        if(role.contains("MO")){
            Mentor mentor = (Mentor) session.getAttribute("authentication");
            Mentor certifiedMentor = mentorRepository.findByMentorIdx(mentor.getMentorIdx());
            if(!checkMatches(password, certifiedMentor.getMember().getPassword())) return false;
            mentorRepository.delete(certifiedMentor);
        } else {
            Mentee mentee = (Mentee) session.getAttribute("authentication");
            Mentee certifiedMentee = menteeRepository.findByMenteeIdx(mentee.getMenteeIdx());
            if(!checkMatches(password, certifiedMentee.getMember().getPassword())) return false;
            menteeRepository.delete(certifiedMentee);
        }
        return true;
    }

    private boolean checkMatches(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }

    public Member checkMember(String userId) {
        return memberRepository.findById(userId).orElse(null);
    }

    @Transactional
    public Member resetPassword(String userId, String resetPw) {
        Member member = memberRepository.findById(userId).orElse(null);
        member.setPassword(passwordEncoder.encode(resetPw));

        return member;
    }

   public Mentor findMentorById(Long mentorIdx) {
        return mentorRepository.findByMentorIdx(mentorIdx);
   }
}
