package com.mm.kim.mentormentee.member.validator;

import com.mm.kim.mentormentee.member.Member;
import lombok.Data;

@Data
public class JoinForm {
    private String userId;
    private String password;
    private String phone;
    private String email;

    public Member convertToMember(){
        Member member = new Member();
        member.setUserId(userId);
        member.setPassword(password);
        member.setEmail(email);
        member.setPhone(phone);
        return member;
    }

}
