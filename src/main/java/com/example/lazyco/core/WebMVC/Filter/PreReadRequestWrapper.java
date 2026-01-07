package com.example.lazyco.core.WebMVC.Filter;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import lombok.Getter;

public class PreReadRequestWrapper extends HttpServletRequestWrapper {

  @Getter private final byte[] requestBody;

  public PreReadRequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
    InputStream requestInputStream = request.getInputStream();
    this.requestBody = requestInputStream.readAllBytes();
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    ByteArrayInputStream byteStream = new ByteArrayInputStream(this.requestBody);
    return new ServletInputStream() {
      @Override
      public int read() throws IOException {
        return byteStream.read();
      }

      @Override
      public boolean isFinished() {
        return byteStream.available() == 0;
      }

      @Override
      public boolean isReady() {
        return true;
      }

      @Override
      public void setReadListener(ReadListener readListener) {
        // Not implemented
      }
    };
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(
        new InputStreamReader(this.getInputStream(), this.getCharacterEncodingSafe()));
  }

  private String getCharacterEncodingSafe() {
    String enc = getCharacterEncoding();
    if (enc == null) {
      return StandardCharsets.UTF_8.name();
    }
    return enc;
  }
}
