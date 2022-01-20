package com.mm.kim.mentormentee.mentoring;

import com.mm.kim.mentormentee.member.Mentee;
import com.mm.kim.mentormentee.member.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentoringHistoryRepository extends JpaRepository<MentoringHistory,String> {
   List<MentoringHistory> findAllByMentor(Mentor mentor);

   List<MentoringHistory> findAllByMentee(Mentee mentee);
}
