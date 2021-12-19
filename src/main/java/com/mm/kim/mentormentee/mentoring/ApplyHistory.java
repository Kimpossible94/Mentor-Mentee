package com.mm.kim.mentormentee.mentoring;

import com.mm.kim.mentormentee.member.Member;
import com.mm.kim.mentormentee.member.Mentee;
import com.mm.kim.mentormentee.member.Mentor;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class ApplyHistory {

    @Id
    @GeneratedValue
    private Long aIdx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "menteeIdx")
    private Mentee mentee;

    @ManyToOne
    @JoinColumn(name = "mentorIdx")
    private Mentor mentor;
    private LocalDate applyDate;
    private LocalDate epDate;
    private int reapplyCnt;

}
