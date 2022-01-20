package com.mm.kim.mentormentee.mentoring;

import com.mm.kim.mentormentee.member.Member;
import com.mm.kim.mentormentee.member.Mentee;
import com.mm.kim.mentormentee.member.Mentor;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class Review {

    @Id
    @GeneratedValue
    private Long reviewIdx;

    @ManyToOne
    @JoinColumn(name = "mentorIdx")
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "menteeIdx")
    private Mentee mentee;

    @Column(columnDefinition = "varchar2(1) default 'N'")
    private String kindness;
    @Column(columnDefinition = "varchar2(1) default 'N'")
    private String communication;
    @Column(columnDefinition = "varchar2(1) default 'N'")
    private String professional;
    @Column(columnDefinition = "varchar2(1) default 'N'")
    private String process;
    @Column(columnDefinition = "varchar2(1) default 'N'")
    private String appointment;
    @Column(columnDefinition = "varchar2(1) default 'N'")
    private String explain;
    @Column(columnDefinition = "varchar2(1) default 'N'", name = "reviewComment")
    private String comment;
    @Column(columnDefinition = "number default 0")
    private int isDel;
}
