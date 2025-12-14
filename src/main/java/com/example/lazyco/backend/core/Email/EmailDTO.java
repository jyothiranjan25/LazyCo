package com.example.lazyco.backend.core.Email;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDTO {
  // Email address fields
  private String from;
  private List<String> to;
  private List<String> cc;
  private List<String> bcc;

  // Email content fields
  private String subject;
  private String bodyText;
  private String bodyHtml;

  // Attachment fields
  private List<byte[]> attachments;
  private String attachmentType;
  private String attachmentName;
}
