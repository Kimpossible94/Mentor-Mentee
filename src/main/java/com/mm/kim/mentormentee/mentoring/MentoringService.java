package com.mm.kim.mentormentee.mentoring;

import com.mm.kim.mentormentee.common.code.Config;
import com.mm.kim.mentormentee.common.code.ErrorCode;
import com.mm.kim.mentormentee.common.exception.HandlableException;
import com.mm.kim.mentormentee.common.mail.EmailSender;
import com.mm.kim.mentormentee.common.util.file.FileInfo;
import com.mm.kim.mentormentee.member.Member;
import com.mm.kim.mentormentee.member.Mentee;
import com.mm.kim.mentormentee.member.Mentor;
import com.mm.kim.mentormentee.member.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MentoringService {
   private final MentoringHistoryRepository mentoringHistoryRepository;
   private final ApplyHistoryRepository applyHistoryRepository;
   private final MentorRepository mentorRepository;
   private final EmailSender emailSender;
   private final RestTemplate http;

   public Map<String, Object> findMentorListByCondition(MentoringCondition condition, Mentee mentee) {
      List<Mentor> mentorList = new ArrayList<Mentor>();
      List<Mentor> excellentMentors = new ArrayList<Mentor>();
      List<Mentor> normalMentors = new ArrayList<Mentor>();
      List<Long> alreadyApplyMentors = new ArrayList<Long>();
      alreadyApplyMentors = findAlreadyapplied(mentee);
      if (alreadyApplyMentors.size() == 0) {
         mentorList = mentorRepository.
                 findAllByUniversityTypeInAndWantTimeInAndRequirementInAndMajorInAndWantDayIn(
                         condition.getUniversityType(), condition.getWantTime(), condition.getWantPlace()
                         , condition.getMajorType(), condition.getWantDate()
                 );
      } else {
         mentorList = mentorRepository.
                 findAllByUniversityTypeInAndWantTimeInAndRequirementInAndMajorInAndWantDayInAndMentorIdxNotIn(
                         condition.getUniversityType(), condition.getWantTime(), condition.getWantPlace()
                         , condition.getMajorType(), condition.getWantDate(), alreadyApplyMentors
                 );
      }


      for (Mentor mentor : mentorList) {
         if (mentor.getMember().getRole().equals("MO01")) {
            excellentMentors.add(mentor);
         } else {
            normalMentors.add(mentor);
         }
      }
      return Map.of("excellentMentors", excellentMentors, "normalMentors", normalMentors);
   }

   private List<Long> findAlreadyapplied(Mentee mentee) {
      List<ApplyHistory> applyHistorieList = applyHistoryRepository.findAllByMentee(mentee);
      List<Long> alreadyApplyMentors = new ArrayList<Long>();
      for (ApplyHistory applyMentor : applyHistorieList) {
         alreadyApplyMentors.add(applyMentor.getMentor().getMentorIdx());
      }
      return alreadyApplyMentors;
   }

   public void applyMentoring(Long mentorIdx, Mentee mentee) {
      List<Long> alreadyApplyMentors = findAlreadyapplied(mentee);
      for (Long appliedMentor : alreadyApplyMentors) {
         if (mentorIdx.equals(appliedMentor)) {
            throw new HandlableException(ErrorCode.ALREADY_APPLY_MENTOR);
         }
      }

      Mentor mentor = mentorRepository.findByMentorIdx(mentorIdx);

      ApplyHistory applyHistory = new ApplyHistory();
      applyHistory.setMentee(mentee);
      applyHistory.setMentor(mentor);
      applyHistoryRepository.save(applyHistory);
      sendNoticeMail(mentor);
   }

   private void sendNoticeMail(Mentor mentor) {
      MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
      body.add("mailTemplate", "notice-apply");

      RequestEntity<MultiValueMap<String, String>> request =
              RequestEntity.post(Config.DOMAIN.DESC + "/mail")
                      .accept(MediaType.APPLICATION_FORM_URLENCODED)
                      .body(body);

      String htmlText = http.exchange(request, String.class).getBody();
      emailSender.send(mentor.getMember().getEmail(), "멘토링 신청이 도착했습니다.", htmlText);
   }

   public Map<String, Object> findHistory(HttpSession session) {
      List<ApplyHistory> applyHistorieList = new ArrayList<ApplyHistory>();
      List<MentoringHistory> mentoringHistoryList = new ArrayList<MentoringHistory>();
      Member member = (Member) session.getAttribute("certified");
      if (member.getRole().contains("MO")) {
         Mentor mentor = (Mentor) session.getAttribute("authentication");
         applyHistorieList = applyHistoryRepository.findAllByMentor(mentor);
         mentoringHistoryList = mentoringHistoryRepository.findAllByMentor(mentor);
      } else {
         Mentee mentee = (Mentee) session.getAttribute("authentication");
         applyHistorieList = applyHistoryRepository.findAllByMentee(mentee);
         mentoringHistoryList = mentoringHistoryRepository.findAllByMentee(mentee);
      }

      return Map.of("applyHistorieList", applyHistorieList, "mentoringHistoryList", mentoringHistoryList);

   }

   @Transactional
   public void reapply(Long ahIdx) {
      ApplyHistory applyHistory = applyHistoryRepository.findByAhIdx(ahIdx);
      applyHistory.setReapplyCnt(applyHistory.getReapplyCnt() + 1);
      applyHistory.setEpDate(applyHistory.getEpDate().plusDays(7));
      sendNoticeMail(applyHistory.getMentor());
   }

   public FileInfo findQrInfo(Long mentorIdx) {
      Mentor mentor = mentorRepository.findByMentorIdx(mentorIdx);
      return mentor.getQrInfo();
   }

   public ApplyHistory findApplyHistoryByIdx(Long ahIdx) {
      return applyHistoryRepository.findByAhIdx(ahIdx);
   }

   public void registMentoring(Long ahIdx, MentoringHistory mentoringHistory, HttpSession session) {
      ApplyHistory applyHistory = applyHistoryRepository.findByAhIdx(ahIdx);
      Mentor mentor = (Mentor) session.getAttribute("authentication");

      if(!mentor.getMember().getUserId().equals(applyHistory.getMentor().getMember().getUserId())){
         throw new HandlableException(ErrorCode.ACCEPT_ONLY_SELF);
      }

      mentoringHistory.setMentor(applyHistory.getMentor());
      mentoringHistory.setMentee(applyHistory.getMentee());
      mentoringHistory.setState("P");
      mentoringHistory.setEpDate(LocalDate.now().plusYears(1));

      mentoringHistoryRepository.save(mentoringHistory);
      applyHistoryRepository.delete(applyHistory);
   }
}
