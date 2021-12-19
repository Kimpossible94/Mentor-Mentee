package com.mm.kim.mentormentee.member;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class Mentor {

    @Id
    @GeneratedValue
    private Long mentorIdx;

    @OneToOne
    @JoinColumn(name = "userIdx")
    private Member member;
    private String universityName;
    private String universityType;
    private int grade;
    private String major;
    private String wantDay;
    private String wantTime;
    private String requirement;
    private String history; //이력
    private int mentoringCnt;
    private int profileImg;
    private String accountNum;
    private String bank;
}
