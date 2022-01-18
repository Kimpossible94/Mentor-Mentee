package com.mm.kim.mentormentee.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class Member {

    @Id
    private String userId;
    private String userName;
    private String password;
    private String email;
    private String gender;
    private String address;
    private String countryCode;
    private String phone;
    private String nickname;
    private String role;
    @Column(columnDefinition = "date default sysdate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate;
    @Column(columnDefinition = "number default 0")
    private int isLeave;
    private String kakaoJoin;
}
