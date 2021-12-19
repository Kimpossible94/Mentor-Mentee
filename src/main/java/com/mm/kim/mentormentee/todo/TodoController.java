package com.mm.kim.mentormentee.todo;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("todo")
public class TodoController {

    private final TodoService todoService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

}
