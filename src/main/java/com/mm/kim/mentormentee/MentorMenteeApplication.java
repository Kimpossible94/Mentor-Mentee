package com.mm.kim.mentormentee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class MentorMenteeApplication {
	public static void main(String[] args) {
		SpringApplication.run(MentorMenteeApplication.class, args);
	}
}
