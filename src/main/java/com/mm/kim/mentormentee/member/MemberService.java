package com.mm.kim.mentormentee.member;

import com.mm.kim.mentormentee.common.code.Config;
import com.mm.kim.mentormentee.common.mail.EmailSender;
import com.mm.kim.mentormentee.member.validator.JoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

        if(memberEntity == null) return null;
        if(!passwordEncoder.matches(member.getPassword(), memberEntity.getPassword())) return null;

        return memberEntity;
    }

    public boolean existsMemberById(String userId) {
        return memberRepository.existsById(userId);
    }

    public void authenticateByEmail(JoinForm form, String token) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("mailTemplate", "join-auth-mail");
        body.add("userId", form.getUserId());
        body.add("persistToken", token);

        RequestEntity<MultiValueMap<String, String>> request =
                RequestEntity.post(Config.DOMAIN.DESC + "/mail")
                        .accept(MediaType.APPLICATION_FORM_URLENCODED)
                        .body(body);

        String htmlText = http.exchange(request, String.class).getBody();
        emailSender.send(form.getEmail(), "회원가입을 축하합니다.", htmlText);
    }

    @Transactional
    public void persistMentor(Mentor mentor) {
        mentor.getMember().setPassword(passwordEncoder.encode(mentor.getMember().getPassword()));
        mentorRepository.save(mentor);
    }

    @Transactional
    public void persistMentee(Mentee mentee) {
        mentee.getMember().setPassword(passwordEncoder.encode(mentee.getMember().getPassword()));
        menteeRepository.save(mentee);
    }

    public Mentor findMentorByMember(Member certifiedMember) {
        return mentorRepository.findMentorByMember(certifiedMember);
    }

    public Mentee findMenteeByMember(Member certifiedMember) {
        return mentorRepository.findMenteeByMember(certifiedMember);
    }
}
