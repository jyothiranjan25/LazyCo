package com.example.lazyco.backend.core.Email.MailConfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class EmailProperties {
  @Value("${email.smtp.host:}")
  private String host;

  @Value("${email.smtp.port:}")
  private int port;

  @Value("${email.smtp.username:}")
  private String username;

  @Value("${email.smtp.password:}")
  private String password;

  @Value("${email.smtp.protocol:smtp}")
  private String protocol;

  @Value("${email.smtp.auth:true}")
  private boolean auth;

  @Value("${email.smtp.starttls.enable:false}")
  private boolean starttlsEnable;

  @Value("${email.smtp.starttls.required:false}")
  private boolean starttlsRequired;

  @Value("${email.smtp.ssl.protocols:TLSv1.2}")
  private String sslProtocols;

  @Value("${email.smtp.connection-timeout:5000}")
  private int connectionTimeout;

  @Value("${email.smtp.timeout:5000}")
  private int timeout;

  @Value("${email.smtp.write-timeout:5000}")
  private int writeTimeout;

  @Value("${email.smtp.from.address:}")
  private String fromAddress;

  @Value("${email.smtp.from.name:}")
  private String fromName;

  @Value("${email.smtp.debug:false}")
  private boolean debug;
}
