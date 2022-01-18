package com.mm.kim.mentormentee.todo;

import com.mm.kim.mentormentee.common.code.ErrorCode;
import com.mm.kim.mentormentee.common.exception.HandlableException;
import com.mm.kim.mentormentee.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

   private final TodoRepository todoRepository;

   public List<Todo> findTodoListByMember(Member member) {
      List<Todo> todoList = new ArrayList<Todo>();
      todoList = todoRepository.findAllByMember(member);
      return todoList;
   }

   public void persistTodo(Member member, Todo todo) {
      todo.setMember(member);
      todoRepository.save(todo);
   }

   public Todo findTodoByIdx(Long todoIdx) {
      return todoRepository.findByTodoIdx(todoIdx).orElseThrow(() -> new HandlableException(ErrorCode.FAILED_LOAD_TODO));
   }

   @Transactional
   public void modifyTodo(Todo todo) {
      Todo curTodo = todoRepository.findByTodoIdx(todo.getTodoIdx()).orElseThrow(() -> new HandlableException(ErrorCode.FAILED_LOAD_TODO));

      if (todo.getTitle() != null) {
         curTodo.setStartDate(todo.getStartDate());
         curTodo.setEndDate(todo.getEndDate());
         curTodo.setTitle(todo.getTitle());
         curTodo.setColor(todo.getColor());
      } else {
         curTodo.setDone(todo.getDone());
      }
   }

   public void deleteTodo(Long todoIdx) {
      Todo todo = todoRepository.findByTodoIdx(todoIdx).orElseThrow(() -> new HandlableException(ErrorCode.FAILED_LOAD_TODO));
      todoRepository.delete(todo);
   }
}
