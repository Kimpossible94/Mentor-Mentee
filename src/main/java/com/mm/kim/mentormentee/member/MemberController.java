package com.mm.kim.mentormentee.member;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @PostMapping("join-mentor")
    public void join(Member member, Mentor mentor){
        mentor.setMember(member);
    }

    @PostMapping("join-mentee")
    public void join(Member member, Mentee mentee){
        mentee.setMember(member);
    }


}
