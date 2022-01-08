package com.mm.kim.mentormentee.board;

import com.mm.kim.mentormentee.member.Member;
import com.mm.kim.mentormentee.member.Mentee;
import com.mm.kim.mentormentee.member.Mentor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("board-list")
    public void boardList(HttpSession session, Model model, String type, Search search){
        System.out.println(search);
        if(type.equals("MO")){
            Mentor mentor = (Mentor) session.getAttribute("authentication");
        } else {
            Mentee mentee = (Mentee) session.getAttribute("authentication");
        }
        model.addAttribute("search", search);
    }

}
