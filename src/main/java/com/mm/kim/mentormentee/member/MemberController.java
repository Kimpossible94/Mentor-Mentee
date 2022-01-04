package com.mm.kim.mentormentee.member;

import com.mm.kim.mentormentee.common.exception.HandlableException;
import com.mm.kim.mentormentee.common.code.ErrorCode;
import com.mm.kim.mentormentee.common.validator.ValidateResult;
import com.mm.kim.mentormentee.member.validator.JoinForm;
import com.mm.kim.mentormentee.member.validator.JoinFormValidator;
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

    public MemberController(MemberService memberService, JoinFormValidator joinFormValidator){
        super();
        this.memberService = memberService;
        this.joinFormValidator = joinFormValidator;
    }

    @InitBinder(value = "joinForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(joinFormValidator);
    }

    @GetMapping("login")
    public void login() {
    }

    @PostMapping("login")
    public String login(Model model, Member inputMember) {
        System.out.println(inputMember.toString());

        Member member = memberService.selectMember(inputMember);
        if (member == null) {
            return "redirect:/member/login?err=1";
        }
        return "redirect:/member/login";
    }

    @GetMapping("join-rule")
    public void joinRule() {}

    @GetMapping("join-form")
    public void joinForm(String type, Model model) {
        if(type.equals("mentor")){
            model.addAttribute("type", "mentor");
        } else {
            model.addAttribute("type", "mentee");
        }
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
    public String join(@Validated JoinForm form, Errors errors, Mentor mentor, Model model, HttpSession session, RedirectAttributes redirectAttr){
        form.setRole("MO00");
        ValidateResult vr = new ValidateResult();

        if(errors.hasErrors()){
            vr.addErrors(errors);
            model.addAttribute("error", vr.getError());
            model.addAttribute("type", "mentor");
            System.out.println(errors.getFieldErrors());
            System.out.println("실행");
            return "/member/join-form";
        }

        mentor.setMember(form.convertToMember());

        String token = UUID.randomUUID().toString();
        session.setAttribute("persistMentor", mentor);
        session.setAttribute("persistToken", token);

        memberService.authenticateByEmail(form, token);

        System.out.println("메일보냄");
        redirectAttr.addAttribute("msg", "회원가입을 위한 이메일이 발송되었습니다.");
        redirectAttr.addAttribute("url", "/");
        return "redirect:/common/result";
    }

    @PostMapping("join-mentee")
    public String join(@Validated JoinForm form, Errors errors, Mentee mentee, Model model, HttpSession session, RedirectAttributes redirectAttr){
        form.setRole("ME00");
        ValidateResult vr = new ValidateResult();

        if(errors.hasErrors()){
            vr.addErrors(errors);
            model.addAttribute("error", vr.getError());
            model.addAttribute("type", "mentor");
            return "/member/join-form";
        }

        mentee.setMember(form.convertToMember());

        String token = UUID.randomUUID().toString();
        session.setAttribute("persistMentee", mentee);
        session.setAttribute("persistToken", token);

        memberService.authenticateByEmail(form, token);

        redirectAttr.addAttribute("msg", "회원가입을 위한 이메일이 발송되었습니다.");
        redirectAttr.addAttribute("url", "/");
        return "redirect:/common/result";
    }

    @GetMapping("join-impl/{token}")
    public String joinImpl(@PathVariable(name = "token", required = false) String token,
                           @SessionAttribute(value = "persistToken", required = false) String persistToken,
                           @SessionAttribute(value = "persistMentor", required = false) Mentor mentor,
                           @SessionAttribute(value = "persistMentee", required = false) Mentee mentee,
                           HttpSession session, RedirectAttributes redirectAttr){

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

        redirectAttr.addAttribute("msg", "회원가입을 환영합니다. 로그인 해주세요");
        redirectAttr.addAttribute("url", "/member/login");
        return "redirect:/common/result";

    }
}
