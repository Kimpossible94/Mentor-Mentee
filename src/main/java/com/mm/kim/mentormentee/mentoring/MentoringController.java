package com.mm.kim.mentormentee.mentoring;

import com.mm.kim.mentormentee.common.code.ErrorCode;
import com.mm.kim.mentormentee.common.exception.HandlableException;
import com.mm.kim.mentormentee.common.util.file.FileInfo;
import com.mm.kim.mentormentee.member.Member;
import com.mm.kim.mentormentee.member.Mentee;
import com.mm.kim.mentormentee.member.Mentor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("mentoring")
public class MentoringController {

   private final MentoringService mentoringService;

   @GetMapping("/choose-condition")
   public void chooseMentorCondition(HttpSession session) {}

   @GetMapping("/mentor-list")
   public void mentorList(MentoringCondition condition, Model model, HttpSession session){
      Mentee mentee = (Mentee) session.getAttribute("authentication");
      model.addAllAttributes(mentoringService.findMentorListByCondition(condition, mentee));
   }

   @GetMapping("apply")
   public String mentorApply(Long mentorIdx, HttpSession session){
      Mentee mentee = (Mentee) session.getAttribute("authentication");
      mentoringService.applyMentoring(mentorIdx, mentee);
      return "/mentoring/apply-complete";
   }

   @GetMapping("manage-page")
   public void managePage(HttpSession session, Model model){
      model.addAllAttributes(mentoringService.findHistory(session));
   }

   @GetMapping("reapply")
   public String reapply(Long ahIdx){
      mentoringService.reapply(ahIdx);
      return "/mentoring/apply-complete";
   }

   @GetMapping("payment")
   public String payment(Long mentorIdx, Model model){
      FileInfo qrInfo = mentoringService.findQrInfo(mentorIdx);
      model.addAttribute("qrInfo", qrInfo);
      return "/mentoring/payment-page";
   }

   @GetMapping("accept-page")
   public void mentoringAccept(Long ahIdx, Model model){
      ApplyHistory applyHistory = mentoringService.findApplyHistoryByIdx(ahIdx);
      model.addAttribute("applyHistory", applyHistory);
   }

   @PostMapping("regist-mentoring")
   public String acceptImpl(Long ahIdx, MentoringHistory mentoringHistory, HttpSession session, Model model){
      mentoringService.registMentoring(ahIdx, mentoringHistory, session);
      model.addAllAttributes(mentoringService.findHistory(session));
      return "/mentoring/manage-page";
   }

   @GetMapping("comment-check")
   @ResponseBody
   public String commentCheck(Long mhIdx, HttpSession session){
      if(!mentoringService.checkComment(mhIdx, session)){
         return "registered";
      }

      return "not-registered";
   }

   @GetMapping("rating-page")
   public void ratingPage(Long mhIdx){}

}
