package com.mm.kim.mentormentee.todo;

import com.mm.kim.mentormentee.member.Member;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class Todo {

    @Id
    @GeneratedValue
    private Long todoIdx;

    @ManyToOne
    @JoinColumn(name = "userIdx")
    private Member member;
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private String done;
    private String color;
}
