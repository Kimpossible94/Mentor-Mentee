package com.mm.kim.mentormentee.mentoring;

import com.mm.kim.mentormentee.member.Member;
import com.mm.kim.mentormentee.member.Mentee;
import com.mm.kim.mentormentee.member.Mentor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
      System.out.println(mentoringService.findHistory(session));
      model.addAllAttributes(mentoringService.findHistory(session));
   }

   @GetMapping("reapply")
   public String reapply(Long ahIdx){
      return "/";
   }

}
