package com.mm.kim.mentormentee.board;

import com.mm.kim.mentormentee.member.Member;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "boardComment")
@DynamicInsert
@DynamicUpdate
public class Comment {
    @Id
    @GeneratedValue
    private Long coIdx;

    @ManyToOne
    @JoinColumn(name = "bdIdx")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private Member member;
    private String coContent;
    private LocalDate regDate;
    private int recommendCnt;

}
