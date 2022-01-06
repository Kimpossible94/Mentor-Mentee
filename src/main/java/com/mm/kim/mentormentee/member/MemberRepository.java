package com.mm.kim.mentormentee.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Member findMemberByUserId(String userId);
}
