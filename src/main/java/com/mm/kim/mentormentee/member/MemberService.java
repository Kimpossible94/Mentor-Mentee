package com.mm.kim.mentormentee.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MenteeRepository menteeRepository;
    private final MentorRepository mentorRepository;

    public Object selectMember(Member member) {
        Mentor mentor = new Mentor();
        return mentor;
    }
}
