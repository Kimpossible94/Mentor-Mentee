package com.mm.kim.mentormentee.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@Component
@ControllerAdvice(basePackages = "com.mm.kim.mentormentee")
public class ExceptionAdvice {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HandlableException.class)
    public String handlableExceptionProcess(HandlableException e, Model model){
        model.addAttribute("msg", e.errorCode.MESSAGE);
        model.addAttribute("url", e.errorCode.URL);
        return "common/result";
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException.class)
    public String dataAccessExceptionProcess(DataAccessException e, Model model){
        e.printStackTrace();
        model.addAttribute("msg", "데이터베이스 접근 중 에러가 발생하였습니다.");
        model.addAttribute("url", "/");
        return "common/result";
    }

}
