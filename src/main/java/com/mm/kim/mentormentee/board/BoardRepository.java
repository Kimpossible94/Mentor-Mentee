package com.mm.kim.mentormentee.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, String> {
   Optional<Board> findByBdIdx(Long bdIdx);

   Board findBoardByComments(Comment comment);

   int countByMenteeNull();

   int countByMentorNull();

   int countByTitleIsContainingAndMentorNull(String word);

   int countByTitleIsContainingAndMenteeNull(String word);

   int countByMentor_Member_UserId(String word);

   int countByMentee_Member_UserId(String word);

   Page<Board> findAllByTitleIsContaining(Pageable viewCount, String word);

   Page<Board> findAllByMentor_Member_UserId(Pageable viewCount, String word);

   Page<Board> findAllByMentee_Member_UserId(Pageable viewCount, String word);
}
