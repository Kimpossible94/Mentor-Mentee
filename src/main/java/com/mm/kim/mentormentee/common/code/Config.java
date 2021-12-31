package com.mm.kim.mentormentee.common.code;

public enum Config {

    //DOMAIN("https://pclass.ga"),
    DOMAIN("http://localhost:9090"),
    COMPANY_EMAIL("zerotiger94@gmail.com"),
    SMTP_AUTHENTICATION_ID("zerotiger94@gmail.com"),
    SMTP_AUTHENTICATION_PASSWORD("A4a5a6a4!"),
    UPLOAD_PATH("C:\\CODE\\upload\\");

    public final String DESC;

    Config(String desc) {
        this.DESC = desc;
    }



}

