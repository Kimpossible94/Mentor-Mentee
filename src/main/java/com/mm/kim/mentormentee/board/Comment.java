package com.mm.kim.mentormentee.board;

import com.mm.kim.mentormentee.member.Member;
import com.mm.kim.mentormentee.member.Mentee;
import com.mm.kim.mentormentee.member.Mentor;
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
    @JoinColumn(name = "mentorIdx")
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "menteeIdx")
    private Mentee mentee;

    @ManyToMany
    private List<Member> recommendMembers;

    private String coContent;
    @Column(columnDefinition = "date default sysdate")
    private LocalDate regDate;
    @Column(columnDefinition = "number default 0")
    private int recommendCnt;

}
