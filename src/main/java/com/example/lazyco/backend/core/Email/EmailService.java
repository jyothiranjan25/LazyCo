package com.example.lazyco.backend.core.Email;

import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  public void sendEmail(EmailDTO emailDTO) {
    BackgroundJob.enqueue(() -> run(emailDTO));
  }

  public void run(EmailDTO emailDTO) {
    System.out.println("Sending email");
  }
}
