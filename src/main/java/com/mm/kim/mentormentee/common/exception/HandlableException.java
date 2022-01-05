package com.mm.kim.mentormentee.common.exception;

import com.mm.kim.mentormentee.common.code.ErrorCode;

public class HandlableException extends RuntimeException{

    private static final long serialVersionUID = -3930409458039432875L;
    public ErrorCode errorCode;

    public HandlableException(ErrorCode error){
        this.errorCode = error;
        this.setStackTrace(new StackTraceElement[0]);
    }

    public HandlableException(ErrorCode error, Exception e){
        this.errorCode = error;
        e.printStackTrace();
        this.setStackTrace(new StackTraceElement[0]);
    }

}
