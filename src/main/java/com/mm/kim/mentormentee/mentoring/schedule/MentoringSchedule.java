package com.mm.kim.mentormentee.mentoring.schedule;

import com.mm.kim.mentormentee.mentoring.MentoringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MentoringSchedule {

   @Autowired
   private MentoringService mentoringService;

   @Scheduled(cron = "0 0 0 * * *")
   public void updateState() throws Exception {
      mentoringService.updateState();
   }
}
