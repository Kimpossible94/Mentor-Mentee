package com.mm.kim.mentormentee.board;

import com.mm.kim.mentormentee.common.code.ErrorCode;
import com.mm.kim.mentormentee.common.exception.HandlableException;
import com.mm.kim.mentormentee.common.util.file.FileInfo;
import com.mm.kim.mentormentee.common.util.file.FileUtil;
import com.mm.kim.mentormentee.common.util.pagination.Paging;
import com.mm.kim.mentormentee.member.Member;
import com.mm.kim.mentormentee.member.Mentee;
import com.mm.kim.mentormentee.member.Mentor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

   private final BoardRepository boardRepository;
   private final CommentRepository commentRepository;

   public void persistBoard(List<MultipartFile> multiparts, Board board) {
      List<FileInfo> fileInfos = new ArrayList<FileInfo>();
      FileUtil fileUtil = new FileUtil();

      for (MultipartFile multipartFile : multiparts) {
         if (!multipartFile.isEmpty()) {
            fileInfos.add(fileUtil.fileUpload(multipartFile));
         }
      }

      board.setFiles(fileInfos);
      boardRepository.save(board);
   }

   public Map<String, Object> findBoardByPage(int pageNumber, String type, Search search, String sort) {
      int cntPerPage = 10;
      Page<Board> page = null;
      Paging pageUtil = null;
      if (search.getCondition() != null) {
         page = pageBySearchAndSort(type, search, sort, cntPerPage, pageNumber);
         if (search.getCondition().equals("bdTitle")) {
            pageUtil = pageUtilByTypeAndCondition("title", cntPerPage, pageNumber, search.getWord(), type);
         } else {
            pageUtil = pageUtilByTypeAndCondition("userId", cntPerPage, pageNumber, search.getWord(), type);
         }
      } else {
         page = pageBySort(pageNumber, cntPerPage, sort);
         pageUtil = pageUtilByTypeAndCondition("bdIdx", cntPerPage, pageNumber, search.getWord(), type);
      }

      List<Board> boardList = page.getContent();
      List<Board> boardListMentor = new ArrayList<Board>();
      List<Board> boardListMentee = new ArrayList<Board>();
      log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> boardList" + boardList);
      for (Board board : boardList) {
         if (board.getMentor() != null) {
            boardListMentor.add(board);
         } else {
            boardListMentee.add(board);
         }
      }

      if (type.contains("MO")) {
         return Map.of("boardList", boardListMentor, "paging", pageUtil);
      } else {
         return Map.of("boardList", boardListMentee, "paging", pageUtil);
      }
   }

   private Page<Board> pageBySearchAndSort(String type, Search search, String sort, int cntPerPage, int pageNumber) {
      Page<Board> page = null;
      if (search.getCondition().equals("bdTitle")) {
         if (sort.equals("view")) {
            page = boardRepository.findAllByTitleIsContainingOrderByViewCountDesc(
                    (Pageable) PageRequest.of(pageNumber - 1, cntPerPage, Sort.Direction.DESC, "title"), search.getWord());
         } else {
            page = boardRepository.findAllByTitleIsContainingOrderByRecCountDesc(
                    (Pageable) PageRequest.of(pageNumber - 1, cntPerPage, Sort.Direction.DESC, "title"), search.getWord());
         }
      } else {
         if (type.contains("MO")) {
            if (sort.equals("view")) {
               page = boardRepository.findAllByMentor_Member_UserIdOrderByViewCountDesc(
                       (Pageable) PageRequest.of(pageNumber - 1, cntPerPage, Sort.Direction.DESC, "mentor_member_userId"), search.getWord());
            } else {
               page = boardRepository.findAllByMentor_Member_UserIdOrderByRecCountDesc(
                       (Pageable) PageRequest.of(pageNumber - 1, cntPerPage, Sort.Direction.DESC, "mentor_member_userId"), search.getWord());
            }
         } else {
            if (sort.equals("view")) {
               page = boardRepository.findAllByMentee_Member_UserIdOrderByViewCountDesc(
                       (Pageable) PageRequest.of(pageNumber - 1, cntPerPage, Sort.Direction.DESC, "mentee_member_userId"), search.getWord());
            } else {
               page = boardRepository.findAllByMentee_Member_UserIdOrderByRecCountDesc(
                       (Pageable) PageRequest.of(pageNumber - 1, cntPerPage, Sort.Direction.DESC, "mentee_member_userId"), search.getWord());
            }
         }
      }
      return page;
   }

   private Page<Board> pageBySort(int pageNumber, int cntPerPage, String sort) {
      Page<Board> page = null;
      log.info(">>>>>>>>>>>>>>>>>> sort" + sort);
      log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>실행");
      if (sort != null) {
         log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>실행2");
         if (sort.equals("view")) {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>실행3");
            page = boardRepository.findAllByBdIdxOrderByViewCountDesc(PageRequest.of(pageNumber - 1, cntPerPage, Sort.Direction.DESC, "bdIdx"));
         } else {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>실행4");
            page = boardRepository.findAllByBdIdxOrderByRecCountDesc(PageRequest.of(pageNumber - 1, cntPerPage, Sort.Direction.DESC, "bdIdx"));
         }
      } else {
         log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>실행5");
         page = boardRepository.findAll(PageRequest.of(pageNumber - 1, cntPerPage, Sort.Direction.DESC, "bdIdx"));
      }
      log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + page.getContent().toString());
      return page;
   }

   private Paging pageUtilByTypeAndCondition(String condition, int cntPerPage, int pageNumber, String word, String
           type) {
      Paging pageUtil = null;
      int blockCnt = 5;
      if (condition.equals("title")) {
         pageUtil = Paging.builder()
                 .total(type.contains("MO") ? boardRepository.countByTitleIsContainingAndMenteeNull(word) :
                         boardRepository.countByTitleIsContainingAndMentorNull(word))
                 .url("/board/board-list")
                 .cntPerPage(cntPerPage)
                 .blockCnt(blockCnt)
                 .curPage(pageNumber)
                 .build();
      } else if (condition.equals("userId")) {
         pageUtil = Paging.builder()
                 .total(type.contains("MO") ? boardRepository.countByMentor_Member_UserId(word) :
                         boardRepository.countByMentee_Member_UserId(word))
                 .url("/board/board-list")
                 .cntPerPage(cntPerPage)
                 .blockCnt(blockCnt)
                 .curPage(pageNumber)
                 .build();
      } else {
         pageUtil = Paging.builder()
                 .total(type.contains("MO") ? boardRepository.countByMenteeNull() : boardRepository.countByMentorNull())
                 .url("/board/board-list")
                 .cntPerPage(cntPerPage)
                 .blockCnt(blockCnt)
                 .curPage(pageNumber)
                 .build();
      }

      return pageUtil;
   }

   @Transactional
   public Board findBoardByBdIdx(Long bdIdx) {
      Board board = boardRepository.findByBdIdx(bdIdx).orElseThrow(() -> new HandlableException(ErrorCode.FAILED_LOAD_BOARD));
      board.setViewCount(board.getViewCount() + 1);
      return board;
   }

   @Transactional
   public Board persistComment(Long bdIdx, String type, String coComment, HttpSession session) {
      Board board = boardRepository.findByBdIdx(bdIdx).orElse(null);
      if (board == null) return null;
      Comment comment = new Comment();
      if (type.equals("MO")) {
         Mentor mentor = (Mentor) session.getAttribute("authentication");
         comment.setMentor(mentor);
      } else {
         Mentee mentee = (Mentee) session.getAttribute("authentication");
         comment.setMentee(mentee);
      }
      comment.setCoContent(coComment);
      List<Comment> commentList = board.getComments();
      commentList.add(comment);
      board.setComments(commentList);

      return board;
   }

   @Transactional
   public Comment recommendComment(Long coIdx, String type, HttpSession session) {
      Comment comment = commentRepository.findByCoIdx(coIdx).orElse(null);
      if (comment == null) return null;

      List<Member> memberList = comment.getRecommendMembers();
      Member sessionMember = new Member();

      if (type.contains("MO")) {
         Mentor mentor = (Mentor) session.getAttribute("authentication");
         sessionMember = mentor.getMember();
      } else {
         Mentee mentee = (Mentee) session.getAttribute("authentication");
         sessionMember = mentee.getMember();
      }

      for (Member member : memberList) {
         if (member.getUserId().equals(sessionMember.getUserId())) {
            comment.setRecommendCnt(comment.getRecommendCnt() - 1);
            memberList.remove(member);
            return comment;
         }
      }

      comment.setRecommendCnt(comment.getRecommendCnt() + 1);
      memberList.add(sessionMember);
      return comment;
   }

   @Transactional
   public String deleteComment(Long coIdx, String type, HttpSession session) {
      Comment comment = commentRepository.findByCoIdx(coIdx).orElse(null);
      Board board = boardRepository.findBoardByComments(comment);

      if (comment == null) {
         return "댓글을 삭제할 수 없습니다.";
      }
      if (type.contains("MO")) {
         Mentor mentor = (Mentor) session.getAttribute("authentication");
         if (!comment.getMentor().getMember().getUserId().equals(mentor.getMember().getUserId())) {
            return "자신의 댓글만 삭제할 수 있습니다.";
         }
      } else {
         Mentee mentee = (Mentee) session.getAttribute("authentication");
         if (!comment.getMentee().getMember().getUserId().equals(mentee.getMember().getUserId())) {
            return "자신의 댓글만 삭제할 수 있습니다.";
         }
      }

      List<Comment> commentList = board.getComments();
      commentList.removeIf(cm -> cm.getCoIdx().equals(coIdx));
      return "댓글을 삭제하였습니다.";
   }

   public void recommendBoard(Long bdIdx, String type, HttpSession session) {
      Board board = boardRepository.findByBdIdx(bdIdx).orElseThrow(() ->
              new HandlableException(ErrorCode.FAILED_RECOMMEND_BOARD.setURL("/board/board-detail?bdIdx=" + bdIdx)));

      List<Member> memberList = board.getRecommendMembers();
      Member sessionMember = new Member();
      if (type.contains("MO")) {
         Mentor mentor = (Mentor) session.getAttribute("authentication");
         sessionMember = mentor.getMember();
      } else {
         Mentee mentee = (Mentee) session.getAttribute("authentication");
         sessionMember = mentee.getMember();
      }

      for (Member member : memberList) {
         if (member.getUserId().equals(sessionMember.getUserId())) {
            board.setRecCount(board.getRecCount() - 1);
            memberList.remove(member);
            board.setRecommendMembers(memberList);
            return;
         }
      }

      board.setRecCount(board.getRecCount() + 1);
      memberList.add(sessionMember);
      board.setRecommendMembers(memberList);
   }

   @Transactional
   public Board modifyBoard(List<Long> flIdxList, Board board, List<MultipartFile> fileList) {
      Board certifiedBoard = boardRepository.findByBdIdx(board.getBdIdx()).orElse(null);
      if (certifiedBoard == null) return null;

      //flIdx똑같으면 삭제
      certifiedBoard.getFiles().removeIf(fileInfo ->
              !fileInfo.getFlIdx().equals(flIdxList.get(certifiedBoard.getFiles().indexOf(fileInfo))));

      List<FileInfo> fileInfos = new ArrayList<FileInfo>();
      FileUtil fileUtil = new FileUtil();

      for (MultipartFile multipartFile : fileList) {
         if (!multipartFile.isEmpty()) {
            certifiedBoard.getFiles().add(fileUtil.fileUpload(multipartFile));
         }
      }

      certifiedBoard.setTitle(board.getTitle());
      certifiedBoard.setBdContent(board.getBdContent());

      return certifiedBoard;

   }

   public boolean checkWriter(Board board, String type, HttpSession session) {
      if (type.contains("MO")) {
         Mentor mentor = (Mentor) session.getAttribute("authentication");
         if (!board.getMentor().getMember().getUserId().equals(mentor.getMember().getUserId())) {
            return false;
         }
      } else {
         Mentee mentee = (Mentee) session.getAttribute("authentication");
         if (!board.getMentee().getMember().getUserId().equals(mentee.getMember().getUserId())) {
            return false;
         }
      }
      return true;
   }

   @Transactional
   public boolean deleteBoard(Long bdIdx, String type, HttpSession session) {
      Board board = findBoardByBdIdx(bdIdx);
      if (!checkWriter(board, type, session)) {
         return false;
      }

      boardRepository.delete(board);
      return true;
   }
}
