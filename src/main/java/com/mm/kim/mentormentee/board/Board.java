package com.mm.kim.mentormentee.board;

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
public class Board {
    @Id
    @GeneratedValue
    private Long bdIdx;
    private String title;
    private LocalDate regDate;
    private int viewCount;
    private int recCount;
    private String bdContent;

    @ManyToOne
    @JoinColumn(name = "mentorIdx")
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "menteeIdx")
    private Mentee mentee;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private Member member;
}
