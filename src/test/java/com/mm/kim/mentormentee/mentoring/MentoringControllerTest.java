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
}
