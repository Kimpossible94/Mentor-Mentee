package com.mm.kim.mentormentee.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorRepository extends JpaRepository<Mentor, String> {
   Mentor findMentorByMember(Member member);

   Mentor findByMentorIdx(Long mentorIdx);

   List<Mentor> findAllByUniversityTypeInAndWantTimeInAndRequirementInAndMajorInAndWantDayInAndMentorIdxNotIn(
           String[] universityType, String[] wantTime, String[] wantPlace, String[] majorType, String[] wantDate
           , List<Long> alreadyApplyMentors);

   List<Mentor> findAllByUniversityTypeInAndWantTimeInAndRequirementInAndMajorInAndWantDayIn(
           String[] universityType, String[] wantTime, String[] wantPlace, String[] majorType, String[] wantDate);
}
