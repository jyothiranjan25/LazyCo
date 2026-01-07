package com.example.lazyco.core.Email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAttachmentDTO {
  private String filename;
  private String contentType;
  private byte[] data;
}
