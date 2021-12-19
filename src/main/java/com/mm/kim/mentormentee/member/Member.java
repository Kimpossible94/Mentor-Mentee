package com.mm.kim.mentormentee.member;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class Member {

    @Id
    @GeneratedValue
    private Long userIdx;
    private String userName;
    private String userId;
    private String password;
    private String email;
    private String gender;
    private String address;
    private String phone;
    private String nickname;
    private String role;
    private LocalDate joinDate;
    private int isLeave;
    private String kakaoJoin;
}
