package com.example.lazyco.core.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailMapper {

  public SimpleMailMessage toSimple(EmailDTO dto) {
    SimpleMailMessage msg = new SimpleMailMessage();
    if (dto.getFrom() != null) msg.setFrom(dto.getFrom());
    if (dto.getReplyTo() != null) msg.setReplyTo(dto.getReplyTo());
    if (dto.getTo() != null) msg.setTo(dto.getTo().toArray(new String[0]));
    if (dto.getCc() != null) msg.setCc(dto.getCc().toArray(new String[0]));
    if (dto.getBcc() != null) msg.setBcc(dto.getBcc().toArray(new String[0]));
    if (dto.getSubject() != null) msg.setSubject(dto.getSubject());
    if (dto.getBodyText() != null) msg.setText(dto.getBodyText());
    return msg;
  }

  public MimeMessage toMime(JavaMailSender mailSender, EmailDTO dto) throws MessagingException {

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    if (dto.getFrom() != null) helper.setFrom(dto.getFrom());
    if (dto.getReplyTo() != null) helper.setReplyTo(dto.getReplyTo());
    if (dto.getTo() != null) helper.setTo(dto.getTo().toArray(new String[0]));
    if (dto.getCc() != null) helper.setCc(dto.getCc().toArray(new String[0]));
    if (dto.getBcc() != null) helper.setBcc(dto.getBcc().toArray(new String[0]));
    if (dto.getSubject() != null) helper.setSubject(dto.getSubject());

    if (dto.getBodyText() != null && dto.getBodyHtml() != null) {
      helper.setText(dto.getBodyText(), dto.getBodyHtml());
    } else if (dto.getBodyText() == null && dto.getBodyHtml() != null) {
      helper.setText(dto.getBodyHtml(), true);
    } else if (dto.getBodyText() != null) {
      helper.setText(dto.getBodyText());
    }

    if (dto.getAttachments() != null) {
      for (EmailAttachmentDTO att : dto.getAttachments()) {
        helper.addAttachment(
            att.getFilename(), new ByteArrayResource(att.getData()), att.getContentType());
      }
    }
    return message;
  }
}
