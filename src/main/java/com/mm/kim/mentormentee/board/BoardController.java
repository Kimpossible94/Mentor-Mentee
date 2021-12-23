package com.mm.kim.mentormentee.board;

import com.mm.kim.mentormentee.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("board-list")
    public void mentorBoard(HttpSession session, Model model){

    }

}
