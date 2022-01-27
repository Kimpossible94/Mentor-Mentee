package com.mm.kim.mentormentee.board;

import com.mm.kim.mentormentee.member.Member;
import com.mm.kim.mentormentee.member.Mentee;
import com.mm.kim.mentormentee.member.Mentor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("게시글 목록 확인")
    public void boardListTest() throws Exception {
        Search search = new Search();
        String sort = "view";
        Mentee mentee = new Mentee();
        Member member = new Member();
        member.setRole("ME00");
        member.setUserId("test");
        mentee.setMember(member);

        mockMvc.perform(get("/board/board-list")
                .param("sort", sort)
                .param("condition", search.getCondition())
                .param("word", search.getWord())
                .sessionAttr("certified", member)
                .sessionAttr("authentication", mentee))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 상세 확인")
    public void boardDetailTest() throws Exception {
        long bdIdx = 3;
        Mentor mentor = new Mentor();
        Member member = new Member();
        member.setUserId("test");
        member.setRole("MO");
        mentor.setMember(member);

        mockMvc.perform(get("/board/board-detail")
                .param("bdIdx", String.valueOf(bdIdx))
                        .sessionAttr("authentication", mentor))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 추천 테스트")
    public void recommendCommentTest() throws Exception{
        long coIdx = 41;
        Mentor mentor = new Mentor();
        Member member = new Member();
        member.setUserId("test");
        member.setRole("MO");
        mentor.setMember(member);

        mockMvc.perform(get("/board/recommend-comment")
                .param("coIdx", String.valueOf(coIdx))
                .sessionAttr("authentication", mentor))
                .andExpect(status().isOk())
                .andDo(print());


    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void deleteBoard() throws Exception{
        String bdIdx = "21";
        String type = "MO01";
        Mentor mentor = new Mentor();
        Member member = new Member();
        member.setUserId("test");
        member.setRole("MO");
        mentor.setMember(member);

        mockMvc.perform(get("/board/delete-board")
                .param("bdIdx", bdIdx)
                .param("type", type)
                .sessionAttr("authentication", mentor))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
