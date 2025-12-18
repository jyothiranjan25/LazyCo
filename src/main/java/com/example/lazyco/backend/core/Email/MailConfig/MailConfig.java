package com.example.lazyco.backend.core.Email.MailConfig;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

  @Bean
  public JavaMailSender javaMailSender(EmailProperties props) {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(props.getHost());
    mailSender.setPort(props.getPort());
    mailSender.setUsername(props.getUsername());
    mailSender.setPassword(props.getPassword());

    Properties p = mailSender.getJavaMailProperties();
    p.put("mail.transport.protocol", props.getProtocol());
    p.put("mail.smtp.auth", String.valueOf(props.isAuth()));
    p.put("mail.smtp.starttls.enable", String.valueOf(props.isStarttlsEnable()));
    p.put("mail.smtp.starttls.required", String.valueOf(props.isStarttlsRequired()));
    p.put("mail.smtp.ssl.protocols", props.getSslProtocols());
    p.put("mail.smtp.connectiontimeout", String.valueOf(props.getConnectionTimeout()));
    p.put("mail.smtp.timeout", String.valueOf(props.getTimeout()));
    p.put("mail.smtp.writetimeout", String.valueOf(props.getWriteTimeout()));
    p.put("mail.debug", String.valueOf(props.isDebug()));

    return mailSender;
  }
}
