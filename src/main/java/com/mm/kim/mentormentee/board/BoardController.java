package com.mm.kim.mentormentee.board;

import com.mm.kim.mentormentee.common.code.ErrorCode;
import com.mm.kim.mentormentee.common.exception.HandlableException;
import com.mm.kim.mentormentee.member.Member;
import com.mm.kim.mentormentee.member.Mentee;
import com.mm.kim.mentormentee.member.Mentor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {

   private final BoardService boardService;

   @GetMapping("board-list")
   public void boardList(Model model, Search search
           , @RequestParam(required = false, defaultValue = "1") int page, String sort
           , HttpSession session) {
      System.out.println(search);
      String type = ((Member) session.getAttribute("certified")).getRole();
      model.addAllAttributes(boardService.findBoardByPage(page, type, search, sort));
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
      Search search = new Search();
      search.setCondition(null);
      model.addAllAttributes(boardService.findBoardByPage(1, type, search, null));
      model.addAttribute("type", type);
      model.addAttribute("search", search);
      return "/board/board-list";
   }

   @GetMapping("board-detail")
   public void boardDetail(Model model, Long bdIdx) {
      Board board = boardService.findBoardByBdIdx(bdIdx);
      model.addAttribute("board", board);
   }

   @PostMapping("regist-comment")
   public String registComment(Model model, Long bdIdx, String type, HttpSession session, String coComment) {
      if (type.equals("MO")) {
         Mentor mentor = (Mentor) session.getAttribute("authentication");
      } else {
         Mentee mentee = (Mentee) session.getAttribute("authentication");
      }
      Board board = boardService.persistComment(bdIdx, type, coComment, session);

      if (board == null) {
         throw new HandlableException(ErrorCode.FAILED_REGIST_BOARD_COMMENT);
      } else {
         model.addAttribute("board", board);
      }

      return "/board/board-detail";
   }

   @GetMapping("recommend-comment")
   @ResponseBody
   public Comment recommendComment(Long coIdx, String type, HttpSession session) {
      Comment comment = boardService.recommendComment(coIdx, type, session);
      if (comment == null) {
         return null;
      }

      Comment newComment = new Comment();
      newComment.setRecommendCnt(comment.getRecommendCnt());
      newComment.setCoIdx(comment.getCoIdx());
      return newComment;
   }

   @GetMapping("delete-comment")
   @ResponseBody
   public String deleteComment(Long coIdx, String type, HttpSession session) {
      String alertMsg = boardService.deleteComment(coIdx, type, session);
      return alertMsg;
   }

   @GetMapping("board-recommend")
   public String recommendBoard(Long bdIdx, String type, HttpSession session, Model model) {
      boardService.recommendBoard(bdIdx, type, session);
      Board board = boardService.findBoardByBdIdx(bdIdx);
      model.addAttribute("board", board);
      return "/board/board-detail";
   }

   @GetMapping("modify-board")
   public String modifyBoard(Long bdIdx, String type, Model model, HttpSession session) {
      Board board = boardService.findBoardByBdIdx(bdIdx);
      boolean flg = boardService.checkWriter(board, type, session);

      if (!flg) {
         model.addAttribute("msg", "게시글 작성자만 수정이 가능합니다.");
         if (type.contains("MO")) {
            model.addAttribute("url", "/board/board-list?type=MO");
         } else {
            model.addAttribute("url", "/board/board-list?type=ME");
         }
         return "/common/result";
      }
      model.addAttribute("board", board);
      return "/board/modify-board";
   }

   @PostMapping("modify-board")
   public String modifyBoardImpl(@RequestParam List<Long> flIdxList, Board board, @RequestParam List<MultipartFile> fileList, Model model) {
      Board modifyBoard = boardService.modifyBoard(flIdxList, board, fileList);
      model.addAttribute("board", modifyBoard);
      return "/board/board-detail";
   }

   @GetMapping("delete-board")
   public String deleteBoard(Long bdIdx, String type, Model model, HttpSession session) {
      boolean flg = boardService.deleteBoard(bdIdx, type, session);

      if (!flg) {
         model.addAttribute("msg", "게시글 작성자만 수정이 가능합니다.");
         if (type.contains("MO")) {
            model.addAttribute("url", "/board/board-list?type=MO");
         } else {
            model.addAttribute("url", "/board/board-list?type=ME");
         }
         return "/common/result";
      }

      Search search = new Search();
      search.setCondition(null);
      String sort = null;
      model.addAllAttributes(boardService.findBoardByPage(1, type, search, sort));
      model.addAttribute("type", type);
      return "/board/board-list";
   }
}
