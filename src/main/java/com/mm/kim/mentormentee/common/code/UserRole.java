package com.mm.kim.mentormentee.common.code;

public enum UserRole {

    MO00("일반멘토", "mentor"),
    MO01("우수멘토", "mentor"),
    ME00("일반멘티", "mentee"),
    ME01("우수멘티", "mentee");

    public final String LEVEL;
    public final String ROLE;

    UserRole(String level, String role) {
        this.LEVEL = level;
        this.ROLE = role;
    }
}
