package com.mm.kim.mentormentee.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/common/result")
    public void result(){}

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
