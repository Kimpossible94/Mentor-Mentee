package com.mm.kim.mentormentee.board;

import com.mm.kim.mentormentee.common.code.ErrorCode;
import com.mm.kim.mentormentee.common.exception.HandlableException;
import com.mm.kim.mentormentee.member.Member;
import com.mm.kim.mentormentee.member.Mentee;
import com.mm.kim.mentormentee.member.Mentor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("board-list")
    public void boardList(HttpSession session, Model model, String type, Search search
            , @RequestParam(required = false, defaultValue = "1") int page) {
        model.addAllAttributes(boardService.findBoardByPage(page, type));
        model.addAttribute("type", type);
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
    public String upload(@RequestParam List<MultipartFile> fileList, Board board, String type, HttpSession session, Model model) {
        if (type.equals("MO")) {
            Mentor mentor = (Mentor) session.getAttribute("authentication");
            board.setMentor(mentor);
        } else {
            Mentee mentee = (Mentee) session.getAttribute("authentication");
            board.setMentee(mentee);
        }
        boardService.persistBoard(fileList, board);
        model.addAllAttributes(boardService.findBoardByPage(1, type));
        model.addAttribute("type", type);
        return "/board/board-list";
    }

    @GetMapping("board-detail")
    public void boardDetail(Model model, Long bdIdx){
        Board board = boardService.findBoardByBdIdx(bdIdx);
        model.addAttribute("board", board);
    }

    @PostMapping("regist-comment")
    public String registComment(Model model, Long bdIdx, String type, HttpSession session, String coComment){
        Member member = new Member();
        if(type.equals("MO")){
            Mentor mentor = (Mentor) session.getAttribute("authentication");
            member = mentor.getMember();
        } else {
            Mentee mentee = (Mentee) session.getAttribute("authentication");
            member = mentee.getMember();
        }
        Board board = boardService.persistComment(bdIdx, member, coComment);

        if(board == null){
            throw new HandlableException(ErrorCode.FAILED_REGIST_BOARD_COMMENT);
        } else {
            model.addAttribute("board", board);
        }

        return "/board/board-detail";
    }

    @PostMapping("recommend-comment")
    @ResponseBody
    public String recommendComment(Model model, Long coIdx, String type, HttpSession session){
        Board board = boardService.recommendComment(coIdx, type, session);
        if(board == null){
            return "unavailable";
        }
        return "available";
    }

    @GetMapping("delete-comment")
    public String deleteComment(Model model, Long coIdx, String type){

        return "/board/board-detail";
    }
}
