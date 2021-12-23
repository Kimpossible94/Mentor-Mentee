package com.mm.kim.mentormentee.common.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class ValidateResult {
    private Map<String,Object> error;

    public ValidateResult() {
        error = new HashMap<String, Object>();
    }

    public void addErrors(Errors errors) {
        for (FieldError fieldError : errors.getFieldErrors()) {
            error.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }

    public Map<String, Object> getError() {
        return error;
    }
}
