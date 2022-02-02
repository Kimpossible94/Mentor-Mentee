package com.mm.kim.mentormentee.mentoring;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class MentoringControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("멘토 리스트 출력 테스트")
    public void ApplyTest() throws Exception {
        Member member = new Member();
        Mentee mentee = new Mentee();
        member.setUserId("mentor5");
        member.setRole("ME00");
        mentee.setMember(member);

        mockMvc.perform(get("/mentoring/mentor-list")
                .param("universityType", "university")
                .param("majorType", "all")
                .param("wantTime", "am")
                .param("wantDate", "all")
                .param("wantPlace", "all")
                        .sessionAttr("certified", member)
                        .sessionAttr("authentication", mentee))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("멘토 관리 페이지 확인")
    public void mentorManageTest() throws Exception{
        Member member = new Member();
        Mentee mentee = new Mentee();
        member.setUserId("test");
        member.setRole("ME00");
        mentee.setMenteeIdx(1L);
        mentee.setMember(member);

        mockMvc.perform(get("/mentoring/manage-page")
                .sessionAttr("certified", member)
                .sessionAttr("authentication", mentee))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("리뷰 체크 테스트")
    public void commentCheckTest() throws Exception{
        Member member = new Member();
        Mentee mentee = new Mentee();
        member.setUserId("test");
        member.setRole("ME00");
        mentee.setMenteeIdx(1L);
        mentee.setMember(member);

        mockMvc.perform(get("/mentoring/comment-check")
                        .sessionAttr("certified", member)
                        .sessionAttr("authentication", mentee)
                        .param("mhIdx", "1"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("리뷰 페이지 테스트")
    public void reviewPage(){

    }

    @Test
    @DisplayName("리뷰 조회페이지 테스트")
    public void checkReview() throws Exception {
        Member member = new Member();
        Mentor mentor = new Mentor();
        member.setUserId("mentor5");
        member.setRole("MO00");
        mentor.setMember(member);
        mentor.setMentorIdx(10L);
        long mentorIdx = 10;

        mockMvc.perform(get("/mentoring/check-review?mentorIdx=" + mentorIdx)
                .sessionAttr("certified", member)
                .sessionAttr("authentication", mentor))
                .andExpect(status().isOk())
                .andDo(print());
    }


}
