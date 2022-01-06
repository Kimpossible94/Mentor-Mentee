package com.mm.kim.mentormentee.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenteeRepository extends JpaRepository<Mentee, String> {
    Mentee findMenteeByMember(Member certifiedMember);

    Mentee findByMenteeIdx(Long menteeIdx);
}
