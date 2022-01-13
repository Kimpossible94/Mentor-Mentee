package com.mm.kim.mentormentee.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, String> {

    Optional<Comment> findByCoIdx(Long coIdx);
}
