package com.mm.kim.mentormentee.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, String> {
    Optional<Board> findByBdIdx(Long bdIdx);

    Board findBoardByComments(Comment comment);

   int countByMenteeNull();

   int countByMentorNull();
}
