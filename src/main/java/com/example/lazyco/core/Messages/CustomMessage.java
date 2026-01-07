package com.example.lazyco.core.Messages;

import com.example.lazyco.core.Messages.YamlConfig.ApplicationText;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class CustomMessage {

  @Setter @Getter String code;
  private Object[] args;
  @Setter boolean returnCodeAsValue = false;

  // when args are passed, code will be treated as format string
  public CustomMessage(String code, Object[] args) {
    setValueFromCode(code);
    this.args = args;
  }

  // when no args are passed, code will be treated as normal string
  public CustomMessage(String code) {
    setValueFromCode(code);
  }

  // Static factory method to create a custom message
  public static CustomMessage getMessage(MessageCodes code) {
    return new CustomMessage(code.getValue());
  }

  // Static factory method to create a custom message with arguments
  public static CustomMessage getMessage(MessageCodes code, Object... args) {
    return new CustomMessage(code.getValue(), args);
  }

  // Static factory method to create a custom message with returnCodeAsValue option
  public static CustomMessage getMessage(String value, boolean returnCodeAsValue) {
    CustomMessage CustomMessage = new CustomMessage();
    CustomMessage.setCode(value);
    CustomMessage.setReturnCodeAsValue(returnCodeAsValue);
    return CustomMessage;
  }

  // Utility methods to get message strings directly
  public static String getMessageString(MessageCodes code) {
    return CustomMessage.getMessage(code).getMessage();
  }

  // Utility methods to get message strings directly with arguments
  public static String getMessageString(MessageCodes code, Object... args) {
    return CustomMessage.getMessage(code, args).getMessage();
  }

  // Set the translated value for the code
  private void setValueFromCode(String code) {
    this.code = setTranslateValueFromCode(code);
  }

  // Translate the message based on the language property
  private String setTranslateValueFromCode(String code) {
    String language = System.getProperty("user.language");
    ApplicationText.Language lang = ApplicationText.Language.fromString(language);

    String message = ApplicationText.get(code, lang);
    return (message == null || message.trim().isEmpty()) ? code : message;
  }

  // Get the final message, formatted if args are present
  private String getMessage() {
    if (returnCodeAsValue) {
      return code;
    }
    if (args == null) {
      return code;
    }
    try {
      return String.format(code, args);
    } catch (Exception e) {
      return code; // return unformatted code if formatting fails
    }
  }
}
