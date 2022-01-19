package com.mm.kim.mentormentee.mentoring;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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
    @JoinColumn(name = "menteeIdx")
    private Mentee mentee;

    @ManyToOne
    @JoinColumn(name = "mentorIdx")
    private Mentor mentor;


    @Column(columnDefinition = "date default sysdate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate applyDate;

    @Column(columnDefinition = "date default sysdate+7")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate epDate;

    @Column(columnDefinition = "number default 0")
    private int reapplyCnt;

}
