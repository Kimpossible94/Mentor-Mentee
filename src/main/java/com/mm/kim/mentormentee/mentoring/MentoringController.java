package com.mm.kim.mentormentee.mentoring;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("mentoring")
public class MentoringController {

    private final MentoringService mentoringService;

}
