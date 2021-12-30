package com.mm.kim.mentormentee.member;

import com.mm.kim.mentormentee.common.validator.ValidateResult;
import com.mm.kim.mentormentee.member.validator.JoinForm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    Logger logger = LoggerFactory.getLogger(this.getClass());

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
    public void join(@Validated JoinForm form, Errors errors, Mentor mentor, Model model, HttpSession session, RedirectAttributes redirectAttr){
        form.setRole("MO00");

        ValidateResult vr = new ValidateResult();
        model.addAttribute("error", vr.getError());
    }

    @PostMapping("join-mentee")
    public void join(@Validated JoinForm form, Errors errors, Mentee mentee){
        form.setRole("ME00");
    }



}
