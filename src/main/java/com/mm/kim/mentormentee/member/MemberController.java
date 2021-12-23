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
    public void login () {}

    @PostMapping("login")
    public String login (Model model, Member member) {

        memberService.selectMember(member);

        return "redirect:/member/login";
    }
}
