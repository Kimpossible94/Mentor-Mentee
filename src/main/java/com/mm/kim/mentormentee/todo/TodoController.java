package com.mm.kim.mentormentee.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.mm.kim.mentormentee.member.Member;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("todo")
public class TodoController {

    private final TodoService todoService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("todo-main")
    public void todoMain(HttpSession session, Model model){
        Member member = (Member) session.getAttribute("certified");
        List<Todo> todoList = todoService.findTodoListByMember(member);
        model.addAttribute("todoList", todoList);
    }

    @GetMapping("todo-detail")
    public void todoDetail(){}

    @GetMapping("todo-insert")
    public void todoInsert(){}

    @PostMapping("insert")
    @ResponseBody
    public void todoInsertImpl(Todo todo, HttpSession session){
        Member member = (Member) session.getAttribute("certified");
        todoService.persistTodo(member, todo);
    }

    @GetMapping("todo-modify/{todoIdx}")
    public String todoModify(@PathVariable(name = "todoIdx") Long todoIdx, Model model){
        Todo todo = todoService.findTodoByIdx(todoIdx);
        model.addAttribute("todo", todo);
        return "/todo/todo-modify";
    }

    @PostMapping("modify")
    @ResponseBody
    public void todoModifyImpl(Todo todo){
        todoService.modifyTodo(todo);
    }

    @GetMapping("todo-list")
    @ResponseBody
    public List<Todo> todoList(HttpSession session){
        Member member = (Member) session.getAttribute("certified");
        List<Todo> todoList = todoService.findTodoListByMember(member);
        return todoList;
    }

    @PostMapping("delete")
    @ResponseBody
    public void todoDelete(Long todoIdx){
        todoService.deleteTodo(todoIdx);
    }

}
