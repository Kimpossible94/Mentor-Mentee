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
        if(type.equals("MO")){
            Mentor mentor = (Mentor) session.getAttribute("authentication");
        } else {
            Mentee mentee = (Mentee) session.getAttribute("authentication");
        }
        Board board = boardService.persistComment(bdIdx, type, coComment, session);

        if(board == null){
            throw new HandlableException(ErrorCode.FAILED_REGIST_BOARD_COMMENT);
        } else {
            model.addAttribute("board", board);
        }

        return "/board/board-detail";
    }

    @GetMapping("recommend-comment")
    @ResponseBody
    public Comment recommendComment(Long coIdx, String type, HttpSession session){
        Comment comment = boardService.recommendComment(coIdx, type, session);
        if(comment == null){
            return null;
        }

        Comment newComment = new Comment();
        newComment.setRecommendCnt(comment.getRecommendCnt());
        newComment.setCoIdx(comment.getCoIdx());
        return newComment;
    }

    @GetMapping("delete-comment")
    @ResponseBody
    public String deleteComment(Long coIdx, String type, HttpSession session){
        String alertMsg = boardService.deleteComment(coIdx, type, session);
        return alertMsg;
    }

    @GetMapping("board-recommend")
    public String recommendBoard(Long bdIdx, String type, HttpSession session, Model model){
        boardService.recommendBoard(bdIdx, type, session);
        Board board = boardService.findBoardByBdIdx(bdIdx);
        model.addAttribute("board", board);
        return "/board/board-detail";
    }

    @GetMapping("modify-board")
    public void modifyBoard(Long bdIdx, String type){


    }
}
