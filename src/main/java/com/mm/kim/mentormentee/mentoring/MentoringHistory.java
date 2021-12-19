package com.mm.kim.mentormentee.mentoring;

import com.mm.kim.mentormentee.member.Member;
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
public class MentoringHistory {
    @Id
    @GeneratedValue
    private Long mIdx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "mentorIdx")
    private Mentor mentor;
    private String state;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate epDate;
    private int price;
}
