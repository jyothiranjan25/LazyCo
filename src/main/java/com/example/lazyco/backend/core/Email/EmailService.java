package com.example.lazyco.backend.core.Email;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.Email.MailConfig.EmailProperties;
import jakarta.mail.internet.MimeMessage;
import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.scheduling.JobBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final EmailProperties emailProperties;
  private final JavaMailSender javaMailSender;
  private final EmailMapper emailMapper;
  private final AbstractAction abstractAction;

  public EmailService(
      EmailProperties emailProperties,
      JavaMailSender javaMailSender,
      EmailMapper emailMapper,
      AbstractAction abstractAction) {
    this.emailProperties = emailProperties;
    this.javaMailSender = javaMailSender;
    this.emailMapper = emailMapper;
    this.abstractAction = abstractAction;
  }

  public void sendSystemMail(EmailDTO emailDTO) {
    String jobName = emailDTO.getSubject() + "::" + String.join(",", emailDTO.getTo());
    JobBuilder jobBuilder =
        JobBuilder.aJob()
            .withName(jobName)
            .withDetails((EmailService service) -> service.runSystemMail(emailDTO));
    BackgroundJob.create(jobBuilder);
  }

  public void runSystemMail(EmailDTO emailDTO) {
    emailDTO.setFrom(emailProperties.getFromAddress());
    try {
      MimeMessage mimeMessage = emailMapper.toMime(javaMailSender, emailDTO);
      javaMailSender.send(mimeMessage);
    } catch (Exception e) {
      throw new RuntimeException("Failed to send email", e);
    }
  }

  public void sendMail(EmailDTO emailDTO) {
    String userId = abstractAction.getLoggedInUser().getEmail();
    String userGroup = abstractAction.getLoggedInUserGroup().getFullyQualifiedName();
    String jobName = emailDTO.getSubject() + "::" + String.join(",", emailDTO.getTo());
    JobBuilder jobBuilder =
        JobBuilder.aJob()
            .withName(jobName)
            .withDetails((EmailService service) -> service.run(emailDTO, userId, userGroup));
    BackgroundJob.create(jobBuilder);
  }

  public void run(EmailDTO emailDTO, String userId, String userGroup) {
    abstractAction.setSystemJobUserContext(userId, userGroup);
    try {
      // @TODO: Add logging
    } finally {
      abstractAction.popSystemJobUserContext();
    }
  }
}
