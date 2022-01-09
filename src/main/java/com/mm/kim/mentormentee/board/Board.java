package com.mm.kim.mentormentee.board;

import com.mm.kim.mentormentee.common.util.file.FileInfo;
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
@DynamicUpdate
@DynamicInsert
public class Board {
    @Id
    @GeneratedValue
    private Long bdIdx;
    private String title;
    @Column(columnDefinition = "date default sysdate")
    private LocalDate regDate;

    @Column(columnDefinition = "number default 0")
    private int viewCount;

    @Column(columnDefinition = "number default 0")
    private int recCount;
    private String bdContent;

    @ManyToOne
    @JoinColumn(name = "mentorIdx")
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "menteeIdx")
    private Mentee mentee;

    @OneToMany(cascade = CascadeType.ALL)
    private List<FileInfo> files;
}
