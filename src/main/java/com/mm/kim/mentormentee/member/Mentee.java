package com.mm.kim.mentormentee.member;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class Mentee {

    @Id
    @GeneratedValue
    private Long menteeIdx;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private Member member;
    private String schoolName;
    private String major;
    private int grade;
    private String hopeUniversity;
    private String hopeMajor;
}
