package com.mm.kim.mentormentee.board;

import com.mm.kim.mentormentee.member.Member;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class RecommendMember {

    @Id
    @GeneratedValue
    private Long rdIdx;

    @ManyToOne
    @JoinColumn(name = "coIdx")
    private Comment comment;

    @OneToOne
    @JoinColumn(name = "userIdx")
    private Member member;

}
