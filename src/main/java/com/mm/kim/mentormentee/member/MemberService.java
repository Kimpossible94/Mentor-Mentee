package com.mm.kim.mentormentee.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MenteeRepository menteeRepository;
    private final MentorRepository mentorRepository;

    public Member selectMember(Member inputMember) {
        Member member = new Member();
        return member;
    }

    public boolean existsMemberById(String userId) {
        return memberRepository.existsById(userId);
    }
}
