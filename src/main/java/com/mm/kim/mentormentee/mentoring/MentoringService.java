package com.mm.kim.mentormentee.mentoring;

import com.mm.kim.mentormentee.member.Mentee;
import com.mm.kim.mentormentee.member.Mentor;
import com.mm.kim.mentormentee.member.MentorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MentoringService {
    private final MentoringHistoryRepository mentoringHistoryRepository;
    private final ApplyHistoryRepository applyHistoryRepository;
    private final MentorRepository mentorRepository;

   public Map<String, Object> findMentorListByCondition(MentoringCondition condition) {
      List<Mentor> mentorList = new ArrayList<Mentor>();
      mentorList = mentorRepository.
              findAllByUniversityTypeInAndWantTimeInAndRequirementInAndMajorInAndWantDayIn(
                      condition.getUniversityType(), condition.getWantTime(), condition.getWantPlace()
                      , condition.getMajorType(), condition.getWantDate()
              );

      List<Mentor> excellentMentors = new ArrayList<Mentor>();
      List<Mentor> normalMentors = new ArrayList<Mentor>();
      for (Mentor mentor : mentorList) {
         if(mentor.getMember().getRole().equals("MO01")){
            excellentMentors.add(mentor);
         } else {
            normalMentors.add(mentor);
         }
      }
      return Map.of("excellentMentors", excellentMentors, "normalMentors", normalMentors);
   }

   public void applyMentoring(Long mentorIdx, Mentee mentee) {
      Mentor mentor = mentorRepository.findByMentorIdx(mentorIdx);
      ApplyHistory applyHistory = new ApplyHistory();
      applyHistory.setMentee(mentee);
      applyHistory.setMentor(mentor);
      applyHistoryRepository.save(applyHistory);
   }
}
