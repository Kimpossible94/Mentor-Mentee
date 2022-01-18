package com.mm.kim.mentormentee.todo;

import com.mm.kim.mentormentee.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, String> {

   List<Todo> findAllByMember(Member member);

   Optional<Todo> findByTodoIdx(Long todoIdx);
}
