package com.mm.kim.mentormentee.member;

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
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("로그인 페이지 접속")
    public void login() throws Exception {
        mockMvc.perform(get("/member/login"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인")
    public void loginImpl() throws Exception {
        String userId = "test";
        String password = "1234";
        mockMvc.perform(post("/member/login")
                .param("userId", userId)
                .param("password", password))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("join-rule 페이지 접속")
    public void joinRule() throws Exception {
        mockMvc.perform(get("/member/join-rule"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 양식 페이지 접속")
    public void joinForm() throws Exception {
        mockMvc.perform(get("/member/join-form")
                        .param("type", "mentor"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("아이디 존재 유무 확인")
    public void idCheck() throws Exception {
        String userId = "test22";

        mockMvc.perform(get("/member/id-check")
                .param("userId", userId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입")
    public void joinMentor() throws Exception {
        mockMvc.perform(post("/member/join-mentee")
                    .param("userId", "test")
                    .param("password", "1234")
                    .param("email", "aaa@bbb.com")
                    .param("phone", "01047178981"))
                .andExpect(status().isOk())
                .andDo(print());
    }

}