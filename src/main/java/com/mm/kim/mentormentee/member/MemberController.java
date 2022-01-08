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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("member")
public class MemberController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private MemberService memberService;
    private JoinFormValidator joinFormValidator;
    private ModifyPasswordValidator passwordValidator;

    public MemberController(MemberService memberService, JoinFormValidator joinFormValidator
            , ModifyPasswordValidator passwordValidator) {
        super();
        this.memberService = memberService;
        this.joinFormValidator = joinFormValidator;
        this.passwordValidator = passwordValidator;
    }

    @InitBinder(value = "joinForm")
    public void initJoiFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(joinFormValidator);
    }

    @InitBinder(value = "modifyPassword")
    public void initPwModifyBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(passwordValidator);
    }

    @GetMapping("login")
    public void login() {
    }

    @PostMapping("login")
    public String loginImpl(RedirectAttributes redirectAttr, Member member, HttpSession session) {

        Member certifiedMember = memberService.authenticateUser(member);
        if (certifiedMember == null) {
            redirectAttr.addFlashAttribute("message", "아이디나 비밀번호가 틀렸습니다.");
            return "redirect:/member/login";
        }

        if (certifiedMember.getRole().contains("MO")) {
            Mentor certifiedMentor = memberService.findMentorByMember(certifiedMember);
            session.setAttribute("authentication", certifiedMentor);
        } else {
            Mentee certifiedMentee = memberService.findMenteeByMember(certifiedMember);
            session.setAttribute("authentication", certifiedMentee);
        }

        return "redirect:/member/mypage";
    }

    @GetMapping("kakao-login/{kakaoId}")
    @ResponseBody
    public Member kakaoLogin(@PathVariable(name = "kakaoId") String id) {
        Member certifiedMember = memberService.findMemberByKaKaoId(id);
        if (certifiedMember == null) {
            Member member = new Member();
            member.setKakaoJoin(id);
            return member;
        }
        certifiedMember.setJoinDate(null);
        return certifiedMember;
    }

    @PostMapping("kakao-login-impl")
    @ResponseBody
    public String kakaoLoginImpl(@RequestBody Member member, HttpSession session){
        System.out.println(member);
        if(member.getRole().contains("MO")){
            Mentor mentor = memberService.findMentorByMember(member);
            session.setAttribute("authentication", mentor);
        } else {
            Mentee mentee = memberService.findMenteeByMember(member);
            session.setAttribute("authentication", mentee);
        }

        return "success";
    }

    @GetMapping("join-rule")
    public String joinRule(String kakao, Model model) {
        if (kakao != null) {
            model.addAttribute("joinKaKao", kakao);
        }
        return "/member/join-rule";
    }

    @GetMapping("join-form")
    public void joinForm(String type, String kakao, Model model) {
        if (kakao != null) {
            JoinForm joinForm = new JoinForm();
            joinForm.setKakaoJoin(kakao);
            model.addAttribute("joinForm", joinForm);
        } else {
            model.addAttribute("joinForm", new JoinForm());
        }
        model.addAttribute("error", new ValidateResult().getError())
                .addAttribute("type", type);
    }

    @GetMapping("id-check")
    @ResponseBody
    public String idCheck(String userId) {
        if (memberService.existsMemberById(userId)) {
            return "not-available";
        }
        return "available";
    }

    @PostMapping("join-mentor")
    public String join(@Validated JoinForm form, Errors errors
            , Mentor mentor, Model model, HttpSession session) {
        form.setRole("MO00");
        ValidateResult vr = new ValidateResult();

        if (errors.hasErrors()) {
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
            , Mentee mentee, Model model, HttpSession session) {
        form.setRole("ME00");
        ValidateResult vr = new ValidateResult();

        if (errors.hasErrors()) {
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

    public void sendEmail(JoinForm form, HttpSession session, Model model, Object mm) {
        String token = UUID.randomUUID().toString();
        if (form.getRole().equals("ME00")) {
            session.setAttribute("persistMentee", (Mentee) mm);
        } else {
            session.setAttribute("persistMentor", (Mentor) mm);
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
                           HttpSession session, Model model) {

        System.out.println("token" + token);
        System.out.println("persistToken" + persistToken);

        if (!token.equals(persistToken)) {
            throw new HandlableException(ErrorCode.AUTHENTICATION_FAILED_ERROR);
        }

        if (mentor != null) {
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
    public String logout(HttpSession session) {
        session.removeAttribute("authentication");
        return "redirect:/";
    }

    @GetMapping("mypage")
    public void mypage(Model model) {
        model.addAttribute(new ModifyPassword());
    }

    @PostMapping("modify-password")
    public String modifyPw(@Validated ModifyPassword modifyPassword, Errors errors, Model model) {

        ValidateResult vr = new ValidateResult();

        if (errors.hasErrors()) {
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

    @PostMapping("modify-mentor")
    public String modifyMentor(Member member, Mentor mentor, Model model, HttpSession session) {
        mentor.setMember(member);
        Mentor modifedMentor = memberService.modifyMentor(mentor);

        session.removeAttribute("authentication");
        session.setAttribute("authentication", modifedMentor);

        model.addAttribute("msg", "회원정보 수정이 완료되었습니다.");
        model.addAttribute("url", "/member/mypage");
        return "/common/result";
    }

    @PostMapping("modify-mentee")
    public String modifyMentee(Member member, Mentee mentee, Model model, HttpSession session) {
        mentee.setMember(member);
        Mentee modifiedMentee = memberService.modifyMentee(mentee);

        session.removeAttribute("authentication");
        session.setAttribute("authentication", modifiedMentee);

        model.addAttribute("msg", "회원정보 수정이 완료되었습니다.");
        model.addAttribute("url", "/member/mypage");
        return "/common/result";
    }

    @PostMapping("modify-account")
    public String modifyAccount(String bank, String accountNum, Model model, HttpSession session) {
        Mentor mentor = (Mentor) session.getAttribute("authentication");
        mentor.setBank(bank);
        mentor.setAccountNum(accountNum);
        Mentor modifiedMentor = memberService.modifyAccount(mentor, bank, accountNum);

        session.removeAttribute("authentication");
        session.setAttribute("authentication", modifiedMentor);

        model.addAttribute("msg", "계좌정보 수정이 완료되었습니다.");
        model.addAttribute("url", "/member/mypage");
        return "/common/result";
    }

    @GetMapping("kakao-auth")
    @ResponseBody
    public String kakaoAuth(String kakao, String type, HttpSession session) {
        if (memberService.findMemberByKaKaoId(kakao) != null) return "unavailable";
        if (type.contains("MO")) {
            Mentor mentor = (Mentor) session.getAttribute("authentication");
            mentor.getMember().setKakaoJoin(kakao);
            memberService.modifyMemberKakaoJoin(mentor.getMember());
        } else {
            Mentee mentee = (Mentee) session.getAttribute("authentication");
            mentee.getMember().setKakaoJoin(kakao);
            memberService.modifyMemberKakaoJoin(mentee.getMember());
        }

        return "available";
    }

    @PostMapping("upload-img")
    public String uploadImg(MultipartFile image, @SessionAttribute(name = "authentication") Mentor mentor
            , HttpSession session) {
        Mentor modifiedMentor = memberService.modifyMentorImage(image, mentor);

        session.removeAttribute("authentication");
        session.setAttribute("authentication", modifiedMentor);

        return "/member/mypage";
    }

    @GetMapping("confirm-pw")
    public void confirmPw() {
    }

    @PostMapping("delete-member")
    public String deleteMember(RedirectAttributes redirectAttr, String password, String role, HttpSession session, Model model) {
        if (!memberService.deleteMember(session, role, password)) {
            redirectAttr.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/member/confirm-pw";
        }

        session.removeAttribute("authentication");

        model.addAttribute("msg", "회원탈퇴 되었습니다.");
        model.addAttribute("url", "/");
        return "/common/result";
    }
}
