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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final RecommendMemberRepository recommendMemberRepository;

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

    public Map<String, Object> findBoardByPage(int pageNumber, String type) {
        int cntPerPage = 10;

        Page<Board> page = boardRepository.findAll(PageRequest.of(pageNumber - 1, cntPerPage, Sort.Direction.DESC, "bdIdx"));
        List<Board> boardList = page.getContent();
        List<Board> boardListMentor = new ArrayList<Board>();
        List<Board> boardListMentee = new ArrayList<Board>();
        for (Board board : boardList) {
            if (board.getMentor() != null) {
                boardListMentor.add(board);
            } else {
                boardListMentee.add(board);
            }
        }

        Paging pageUtil = null;

        if (type.equals("MO")) {
            pageUtil = Paging.builder()
                    .total(boardListMentor.size())
                    .url("/board/board-list")
                    .cntPerPage(cntPerPage)
                    .blockCnt(5)
                    .curPage(pageNumber)
                    .build();
            return Map.of("boardList", boardListMentor, "paging", pageUtil);
        } else {
            pageUtil = Paging.builder()
                    .total(boardListMentee.size())
                    .url("/board/board-list")
                    .cntPerPage(cntPerPage)
                    .blockCnt(5)
                    .curPage(pageNumber)
                    .build();
            return Map.of("boardList", boardListMentee, "paging", pageUtil);
        }
    }

    public Board findBoardByBdIdx(Long bdIdx) {
        return boardRepository.findByBdIdx(bdIdx).orElseThrow(() -> new HandlableException(ErrorCode.FAILED_LOAD_BOARD));
    }

    @Transactional
    public Board persistComment(Long bdIdx, Member member, String coComment) {
        Board board = boardRepository.findByBdIdx(bdIdx).orElse(null);
        Comment comment = new Comment();
        comment.setMember(member);
        comment.setCoContent(coComment);
        if (board != null) {
            List<Comment> commentList = new ArrayList<Comment>();
            commentList.add(comment);
            board.setComments(commentList);
        }

        return board;
    }

    @Transactional
    public Board recommendComment(Long coIdx, String type, HttpSession session) {
        Comment comment = commentRepository.findByCoIdx(coIdx).orElse(null);
        //comment 없을 때 리턴
        if (comment == null) return null;

        Board board = boardRepository.findBoardByComments(comment);
        Member member = new Member();
        if (type.equals("MO")) {
            Mentor mentor = (Mentor) session.getAttribute("authentication");
            member = mentor.getMember();
        } else {
            Mentee mentee = (Mentee) session.getAttribute("authentication");
            member = mentee.getMember();
        }

        //추천한 멤버 등록하기
        ArrayList<RecommendMember> recommendMembers = (ArrayList<RecommendMember>) comment.getRecommendMembers();
        RecommendMember recommendMember = new RecommendMember();

        //이미 추천을 했는지 비교하고 추천했으면 RecommendMember에서 삭제하고 추천수 -1해줌
        for (RecommendMember rdMember : comment.getRecommendMembers()) {
            if (rdMember.getMember().getUserId().equals(member.getUserId())) {
                comment.setRecommendCnt(comment.getRecommendCnt() - 1);
                recommendMemberRepository.deleteByCommentAndMember(comment, member);
                return board;
            }
        }

        comment.setRecommendCnt(comment.getRecommendCnt() + 1);
        recommendMember.setMember(member);
        recommendMember.setComment(comment);
        recommendMembers.add(recommendMember);
        comment.setRecommendMembers(recommendMembers);

        return board;

    }
}
