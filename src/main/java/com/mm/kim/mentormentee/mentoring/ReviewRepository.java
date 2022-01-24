package com.mm.kim.mentormentee.mentoring;

import com.mm.kim.mentormentee.member.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
   Review findByMentoringHistory(MentoringHistory mentoringHistory);

   List<Review> findAllByMentor(Mentor mentor);
}
