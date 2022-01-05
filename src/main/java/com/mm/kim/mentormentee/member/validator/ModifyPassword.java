package com.mm.kim.mentormentee.member.validator;

import lombok.Data;

@Data
public class ModifyPassword {
    private String userId;
    private String curPw;
    private String newPw;
    private String confirmNewPw;
}
