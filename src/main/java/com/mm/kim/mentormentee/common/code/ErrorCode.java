package com.mm.kim.mentormentee.common.code;

public enum ErrorCode {

    DATABASE_ACCESS_ERROR("데이터베이스와 통신 중 에러가 발생하였습니다."),
    VALIDATOR_FAIL_ERROR("부적절한 양식의 데이터 입니다."),
    MAIL_SEND_FAIL_ERROR("이메일 발송 중 에러가 발생하였습니다."),
    HTTP_CONNECT_ERROR("HTTP 통신 중 에러가 발생하였습니다."),
    AUTHENTICATION_FAILED_ERROR("유효하지 않은 인증입니다."),
    UNAUTHORIZED_PAGE_ERROR("접근 권한이 없는 페이지 입니다."),
    UNLOGINED_ERROR("로그인이 필요합니다.","/member/login"),
    ALREADY_REGISTERED_COMMENT("이미 이 멘토에 대한 평가를 등록 하셨습니다.","/mentoring/manage-page"),
    ACCESS_ONLY_MENTOR("멘토만 접근할 수 있습니다."),
    ACCESS_ONLY_MENTEE("멘티만 접근할 수 있습니다."),
    FAILED_FILE_UPLOAD_ERROR("파일업로드에 실패하였습니다."),
    FAILED_LOAD_BOARD("게시글을 불러올 수 없습니다."),
    FAILED_REGIST_BOARD_COMMENT("댓글을 등록할 수 없습니다."),
    FAILED_RECOMMEND_BOARD("게시글을 추천할 수 없습니다."),
    REDIRECT("");


    public final String MESSAGE;
    public String URL;

    private ErrorCode(String msg) {
        this.MESSAGE = msg;
        this.URL = "/";
    }

    private ErrorCode(String msg, String url){
        this.MESSAGE = msg;
        this.URL = url;
    }

    public ErrorCode setURL(String url){
        this.URL = url;
        return this;
    }

}
