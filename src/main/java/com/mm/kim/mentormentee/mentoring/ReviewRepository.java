package com.mm.kim.mentormentee.mentoring;

import com.mm.kim.mentormentee.member.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
   Review findByMentoringHistory(MentoringHistory mentoringHistory);

   List<Review> findAllByMentor(Mentor mentor);

   int countByMentor_MentorIdx(long mentorIdx);

   Page<Review> findAllByMentor(Pageable reviewIdx, Mentor mentor);
}
