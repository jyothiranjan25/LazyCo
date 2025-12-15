package com.example.lazyco.backend.core.Email;

import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.scheduling.JobBuilder;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  public void sendEmail(EmailDTO emailDTO) {
    String jobName = emailDTO.getSubject() + "::" + String.join(",", emailDTO.getTo());
    JobBuilder jobBuilder = JobBuilder.aJob().withName(jobName).withDetails(() -> run(emailDTO));
    BackgroundJob.create(jobBuilder);
  }

  public void run(EmailDTO emailDTO) {
    System.out.println("Sending email");
  } 
}
