package com.mm.kim.mentormentee.member.validator;

import com.mm.kim.mentormentee.member.Member;
import com.mm.kim.mentormentee.member.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class ModifyPasswordValidator implements Validator {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public ModifyPasswordValidator(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ModifyPassword.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ModifyPassword modifyForm = (ModifyPassword) target;

        System.out.println(modifyForm.toString());

        Member certifiedMember = memberRepository.findById(modifyForm.getUserId()).orElseThrow();

        //비밀번호 일지 확인
        if(!passwordEncoder.matches(modifyForm.getCurPw(), certifiedMember.getPassword())){
            errors.rejectValue("curPw", "err-curPw", "비밀번호가 틀렸습니다.");
        }
        
        //이전 비밀번호와 일치 확인
        if(passwordEncoder.matches(modifyForm.getNewPw(), certifiedMember.getPassword())){
            errors.rejectValue("newPw", "err-newPw", "이전과 비밀번호가 동일합니다 이전과 다른 비밀번호로 설정해주세요.");
        }

        // 비밀번호가 8글자 이상, 숫자 영문자 특수문자 조합인지 확인
        boolean valid = Pattern.matches("(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Zㄱ-힣0-9]).{8,}", modifyForm.getNewPw());
        if(!valid){
            errors.rejectValue("newPw", "err-newPw", "비밀번호는 8글자 이상의 숫자 영문자 특수문자 조합입니다.");
        }

        //
        if(!modifyForm.getNewPw().equals(modifyForm.getConfirmNewPw())){
            errors.rejectValue("newPw", "err-newPw", "비밀번호가 일치하지 않습니다.");
        }
    }
}
