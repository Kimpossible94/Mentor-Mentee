package com.mm.kim.mentormentee.common.interceptor;

import com.mm.kim.mentormentee.common.code.ErrorCode;
import com.mm.kim.mentormentee.common.exception.HandlableException;
import com.mm.kim.mentormentee.member.Member;
import com.mm.kim.mentormentee.member.Mentor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String[] uriArr = request.getRequestURI().split("/");

        Mentor mentor = new Mentor();

        if (uriArr.length != 0) {
            switch (uriArr[1]) {
                case "member":
                    memberAuthorize(request, response, uriArr);
                    break;
                case "board":
                case "todo":
                    loginAuthorize(request);
                    break;
                case "mentoring":
                    mentoringAuthorize(request, response, uriArr);
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private void loginAuthorize(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object user = session.getAttribute("authentication");
        if (user == null) {
            throw new HandlableException(ErrorCode.UNLOGINED_ERROR);
        }
    }

    private void mentoringAuthorize(HttpServletRequest request, HttpServletResponse response, String[] uriArr) {
        loginAuthorize(request);

        switch (uriArr[2]) {
            case "mentoring-accept":
            case "regist-mentoring":
                checkMentor(request);
                break;
            case "rating-page":
            case "mentor-list":
            case "regist-apply":
            case "apply-complete":
            case "apply-page":
            case "reapply-complete":
                checkMentee(request);
                break;
            default:
                break;
        }

    }

    private void checkMentor(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member member = new Member();
        if(!member.getRole().equals("MO00") || !member.getRole().equals("MO01")){
            throw new HandlableException(ErrorCode.ACCESS_ONLY_MENTOR);
        }
    }

    private void checkMentee(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member member = new Member();
        if(!member.getRole().equals("ME00") || !member.getRole().equals("ME01")){
            throw new HandlableException(ErrorCode.ACCESS_ONLY_MENTEE);
        }
    }

    private void memberAuthorize(HttpServletRequest request, HttpServletResponse response, String[] uriArr) {
        switch (uriArr[2]) {
            case "mypage":
            case "password-impl":
            case "logout":
                loginAuthorize(request);
            default: break;
        }
    }
}
