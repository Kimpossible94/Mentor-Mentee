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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("board-list")
    public void boardList(HttpSession session, Model model, String type, Search search) {
        System.out.println("-===-=--=-=-=-=-=========================== " + type);
        if (type.equals("MO")) {
            Mentor mentor = (Mentor) session.getAttribute("authentication");
        } else {
            Mentee mentee = (Mentee) session.getAttribute("authentication");
        }
        model.addAttribute("search", search);
    }

    @GetMapping("create-form")
    public void createForm(HttpSession session, Model model, String type) {
        Board board = new Board();
        if (type.equals("MO")) {
            Mentor mentor = (Mentor) session.getAttribute("authentication");
            model.addAttribute("type", type);
        } else {
            Mentee mentee = (Mentee) session.getAttribute("authentication");
        }

        model.addAttribute("type", type);
    }

    @PostMapping("upload")
    public String upload(@RequestParam List<MultipartFile> fileList, Board board, String type, HttpSession session) {
        if (type.equals("MO")) {
            Mentor mentor = (Mentor) session.getAttribute("authentication");
            board.setMentor(mentor);
        } else {
            Mentee mentee = (Mentee) session.getAttribute("authentication");
            board.setMentee(mentee);
        }
        boardService.persistBoard(fileList, board);
        return "/board/board-list";
    }
}
