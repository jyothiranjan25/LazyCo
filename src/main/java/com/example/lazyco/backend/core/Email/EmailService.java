package com.example.lazyco.backend.core.Email;

import com.example.lazyco.backend.core.Email.MailConfig.EmailProperties;
import jakarta.mail.internet.MimeMessage;
import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.scheduling.JobBuilder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final EmailProperties emailProperties;
  private final JavaMailSender javaMailSender;
  private final EmailMapper emailMapper;

  public EmailService(
      EmailProperties emailProperties, JavaMailSender javaMailSender, EmailMapper emailMapper) {
    this.emailProperties = emailProperties;
    this.javaMailSender = javaMailSender;
    this.emailMapper = emailMapper;
  }

  public void sendEmail(EmailDTO emailDTO) {
    String jobName = emailDTO.getSubject() + "::" + String.join(",", emailDTO.getTo());
    JobBuilder jobBuilder =
        JobBuilder.aJob()
            .withName(jobName)
            .withDetails((EmailService service) -> service.run(emailDTO));
    BackgroundJob.create(jobBuilder);
  }

  public void run(EmailDTO emailDTO) {
    emailDTO.setFrom(emailProperties.getFromAddress());
    try {
      if (emailDTO.getBodyHtml() != null
          || (emailDTO.getAttachments() != null && !emailDTO.getAttachments().isEmpty())) {
        MimeMessage mimeMessage = emailMapper.toMime(javaMailSender, emailDTO);
        javaMailSender.send(mimeMessage);
      } else {
        SimpleMailMessage simpleMailMessage = emailMapper.toSimple(emailDTO);
        javaMailSender.send(simpleMailMessage);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to send email", e);
    }
  }
}
