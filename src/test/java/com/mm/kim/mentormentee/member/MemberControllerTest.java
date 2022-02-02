package com.mm.kim.mentormentee.member;

import com.mm.kim.mentormentee.member.validator.ModifyPassword;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
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
        String userId = "test1";
        String password = "123abc!@";
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
                    .param("userId", "test1234")
                    .param("password", "123abc!@")
                    .param("email", "zerotiger94@gmail.com")
                    .param("phone", "11112222"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("이메일 발송 후 회원가입 테스트")
    public void joinImpl() throws Exception{
        Member member = new Member();
        Mentor mentor = new Mentor();
        Mentee mentee = new Mentee();
        member.setUserId("awesomeBoy");
        member.setPassword("1234");
        member.setEmail("kimpossible94@naver.com");
        member.setPhone("00110011");
        member.setCountryCode("010");
        member.setAddress("춘천");
        member.setUserName("김춘천5");
        member.setNickname("춘천지박령");

        // 멘토용 설정
//        member.setRole("MO00");
//        mentor.setMember(member);
//        mentor.setGrade(2);
//        mentor.setBank("카카오뱅크");
//        mentor.setAccountNum("3333072781272");
//        mentor.setHistory("토익 스피킹 level 7, 토익 930점, 전기기사");
////        mentor.setHistory("토익 930점, 화공기사, 수질기사 ,대기기사");
////        mentor.setHistory("포토샵, 대외활동 다수, 포크레인 기사 자격증, 해양구조 자격증, 다양한 기업의 서포터 활동");
//        mentor.setWantDay("sat"); // all, mon, tue, wed, thu, fri, sat, sun
//        mentor.setWantTime("pm"); // all, am, pm, evening
//        mentor.setUniversityType("college"); //university, college
//        mentor.setUniversityName("닭갈비대학교");
//        mentor.setMentoringCnt(0);
//        mentor.setRequirement("myTown"); //all, videoChat, myTown, yourTown, rentalSpace
//        mentor.setMajor("humanities"); //humanities, education, engineering, society, nature, anp, medicine

        // 멘티용 설정
        member.setRole("ME00");
        mentee.setMember(member);
        mentee.setSchoolName("동탄고등학교");
        mentee.setGrade(3);
        mentee.setHopeUniversity("서울대학교");
        mentee.setHopeMajor("컴퓨터공학과");

        mockMvc.perform(get("/member/join-impl/1234")
                .sessionAttr("persistToken", "1234")
                .sessionAttr("persistMentee", mentee))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("마이페이지 접속 확인")
    public void mypage() throws Exception{
        Member member = new Member();
        member.setRole("ME00");
        member.setUserName("김영범");
        Mentor mentor = new Mentor();
        mentor.setMember(member);

        mockMvc.perform(get("/member/mypage")
                        .sessionAttr("authentication", member))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("마이페이지 비밀번호 변경 확인")
    public void modifyPw() throws Exception {
        String userId = "test1";
        String curPw = "123abc!@";
        String newPw = "123abc!!";
        String confirmNewPw = "123abc!!";

        mockMvc.perform(post("/member/modify-password")
                .param("userId", userId)
                .param("curPw", curPw)
                .param("newPw", newPw)
                .param("confirmNewPw", confirmNewPw))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("계좌정보 변경 테스트")
    public void modifyAccount() throws Exception{

    }

    @Test
    @DisplayName("카카오 로그인 추가 테스트")
    public void kakaoAuthTest() throws Exception{
        Member member = new Member();
        member.setUserId("test1");
        String kakaoId = "123111111";

        mockMvc.perform(get("/member/kakao-auth/" + kakaoId)
                .sessionAttr("authentication", member))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("비밀번호 찾기 페이지")
    public void forgetPW() throws Exception{
        mockMvc.perform(get("/member/forget-pw"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("비밀번호 초기화 테스트")
    public void resetPwImplTest() throws Exception{
        mockMvc.perform(get("/member/reset-pw-impl/1234/test")
                        .sessionAttr("resetToken", "1234"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}