package com.mm.kim.mentormentee.board;

import com.mm.kim.mentormentee.member.Member;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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
    @JoinColumn(name = "userIdx")
    private Member member;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "rdIdx")
    private List<RecommendMember> recommendMembers;

    private String coContent;
    @Column(columnDefinition = "date default sysdate")
    private LocalDate regDate;
    @Column(columnDefinition = "number default 0")
    private int recommendCnt;

}
