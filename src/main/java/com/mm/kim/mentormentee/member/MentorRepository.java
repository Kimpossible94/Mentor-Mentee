package com.mm.kim.mentormentee.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor,String> {
    Mentor findMentorByMember(Member member);
}
