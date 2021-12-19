package com.mm.kim.mentormentee.mentoring;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentoringService {
    private final MentoringHistoryRepository mentoringHistoryRepository;
    private final ApplyHistoryRepository applyHistoryRepository;
}
