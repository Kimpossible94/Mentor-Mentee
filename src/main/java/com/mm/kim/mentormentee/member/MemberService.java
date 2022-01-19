package com.mm.kim.mentormentee.member;

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

import javax.servlet.http.HttpSession;

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
        mentorRepository.save(mentor);
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
