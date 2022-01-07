package com.mm.kim.mentormentee.member;

import com.mm.kim.mentormentee.common.util.file.FileInfo;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
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
    private String accountNum;
    private String bank;

    @OneToOne(cascade = CascadeType.ALL)
    private FileInfo fileInfo;
}
