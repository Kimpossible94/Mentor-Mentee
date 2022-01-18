package com.mm.kim.mentormentee.todo;

import com.mm.kim.mentormentee.member.Member;
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
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("할일 메인 페이지 접속")
    public void todoMain() throws Exception{
        Member member = new Member();
        member.setUserId("test");
        mockMvc.perform(get("/todo/todo-main")
                        .sessionAttr("certified", member))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("할일 수정 페이지 접속")
    public void todoModify() throws Exception{
        Member member = new Member();
        member.setUserId("test");
        mockMvc.perform(get("/todo/todo-modify/241")
                        .sessionAttr("certified", member))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }
}