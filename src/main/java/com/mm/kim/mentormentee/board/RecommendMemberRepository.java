package com.mm.kim.mentormentee.board;

import com.mm.kim.mentormentee.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendMemberRepository extends JpaRepository<RecommendMember, String> {
    void deleteByCommentAndMember(Comment comment, Member member);
}
