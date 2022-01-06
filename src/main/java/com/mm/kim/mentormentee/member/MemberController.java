package com.mm.kim.mentormentee.member;

import com.mm.kim.mentormentee.common.exception.HandlableException;
import com.mm.kim.mentormentee.common.code.ErrorCode;
import com.mm.kim.mentormentee.common.validator.ValidateResult;
import com.mm.kim.mentormentee.member.validator.JoinForm;
import com.mm.kim.mentormentee.member.validator.JoinFormValidator;
import com.mm.kim.mentormentee.member.validator.ModifyPassword;
import com.mm.kim.mentormentee.member.validator.ModifyPasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("member")
public class MemberController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private MemberService memberService;
    private JoinFormValidator joinFormValidator;
    private ModifyPasswordValidator passwordValidator;

    public MemberController(MemberService memberService, JoinFormValidator joinFormValidator
            , ModifyPasswordValidator passwordValidator){
        super();
        this.memberService = memberService;
        this.joinFormValidator = joinFormValidator;
        this.passwordValidator = passwordValidator;
    }

    @InitBinder(value = "joinForm")
    public void initJoiFormBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(joinFormValidator);
    }

    @InitBinder(value = "modifyPassword")
    public void initPwModifyBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(passwordValidator);
    }

    @GetMapping("login")
    public void login() {}

    @PostMapping("login")
    public String loginImpl(RedirectAttributes redirectAttr, Member member, HttpSession session) {

        Member certifiedMember = memberService.authenticateUser(member);
        if (certifiedMember == null) {
            redirectAttr.addFlashAttribute("message", "아이디나 비밀번호가 틀렸습니다.");
            return "redirect:/member/login";
        }

        if(certifiedMember.getRole().contains("MO")){
            Mentor certifiedMentor = memberService.findMentorByMember(certifiedMember);
            session.setAttribute("authentication", certifiedMentor);
        } else {
            Mentee certifiedMentee = memberService.findMenteeByMember(certifiedMember);
            session.setAttribute("authentication", certifiedMentee);
        }

        return "redirect:/member/mypage";
    }

    @GetMapping("join-rule")
    public void joinRule() {}

    @GetMapping("join-form")
    public void joinForm(String type, Model model) {
        model.addAttribute("joinForm", new JoinForm())
                .addAttribute("error",new ValidateResult().getError())
                .addAttribute("type", type);
    }

    @GetMapping("id-check")
    @ResponseBody
    public String idCheck(String userId){
        if(memberService.existsMemberById(userId)){
            return "not-available";
        }
        return "available";
    }

    @PostMapping("join-mentor")
    public String join(@Validated JoinForm form, Errors errors
            , Mentor mentor, Model model, HttpSession session){
        form.setRole("MO00");
        ValidateResult vr = new ValidateResult();

        if(errors.hasErrors()){
            vr.addErrors(errors);
            model.addAttribute("joinForm", form)
                    .addAttribute("error", vr.getError())
                    .addAttribute("type", "mentor");
            return "/member/join-form";
        }

        mentor.setMember(form.convertToMember());

        sendEmail(form, session, model, mentor);
        return "/common/result";
    }

    @PostMapping("join-mentee")
    public String join(@Validated JoinForm form, Errors errors
            , Mentee mentee, Model model, HttpSession session){
        form.setRole("ME00");
        ValidateResult vr = new ValidateResult();

        if(errors.hasErrors()){
            vr.addErrors(errors);
            model.addAttribute("joinForm", form)
                    .addAttribute("error", vr.getError())
                    .addAttribute("type", "mentee");
            return "/member/join-form";
        }

        mentee.setMember(form.convertToMember());

        sendEmail(form, session, model, mentee);

        return "/common/result";
    }

    public void sendEmail(JoinForm form, HttpSession session, Model model, Object mm){
        String token = UUID.randomUUID().toString();
        if(form.getRole().equals("ME00")){
            session.setAttribute("persistMentee", (Mentee)mm);
        } else {
            session.setAttribute("persistMentor", (Mentor)mm);
        }
        session.setAttribute("persistToken", token);

        memberService.authenticateByEmail(form, token);

        model.addAttribute("msg", "회원가입을 위한 이메일이 발송되었습니다.");
        model.addAttribute("url", "/index");
    }

    @GetMapping("join-impl/{token}")
    public String joinImpl(@PathVariable(name = "token", required = false) String token,
                           @SessionAttribute(value = "persistToken", required = false) String persistToken,
                           @SessionAttribute(value = "persistMentor", required = false) Mentor mentor,
                           @SessionAttribute(value = "persistMentee", required = false) Mentee mentee,
                           HttpSession session, Model model){

        System.out.println("token" + token);
        System.out.println("persistToken" + persistToken);
        System.out.println(mentor);

        if(!token.equals(persistToken)){
            throw new HandlableException(ErrorCode.AUTHENTICATION_FAILED_ERROR);
        }

        if(mentor != null){
            memberService.persistMentor(mentor);
            session.removeAttribute("persistMentor");
        } else {
            memberService.persistMentee(mentee);
            session.removeAttribute("persistMentee");
        }

        session.removeAttribute("persistToken");

        model.addAttribute("msg", "회원가입을 환영합니다. 로그인 해주세요");
        model.addAttribute("url", "/member/login");
        return "common/result";

    }

    @GetMapping("logout")
    public String logout(HttpSession session){
        session.removeAttribute("authentication");
        return "redirect:/";
    }

    @GetMapping("mypage")
    public void mypage(Model model){
        model.addAttribute(new ModifyPassword());
    }

    @PostMapping("modify-password")
    public String modifyPw(@Validated ModifyPassword modifyPassword, Errors errors, Model model){

        ValidateResult vr = new ValidateResult();

        if(errors.hasErrors()){
            vr.addErrors(errors);
            model.addAttribute(new ModifyPassword())
                    .addAttribute("error", vr.getError());
            return "/member/mypage";
        }

        memberService.modifyPassword(modifyPassword);

        model.addAttribute("msg", "비밀번호 수정이 완료되었습니다.");
        model.addAttribute("url", "/member/mypage");
        return "/common/result";
    }

}
