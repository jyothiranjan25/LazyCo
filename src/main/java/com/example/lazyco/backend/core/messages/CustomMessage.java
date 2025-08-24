package com.example.lazyco.backend.core.messages;

import com.example.lazyco.backend.core.messages.yamlconfig.ApplicationText;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class CustomMessage {
    @Setter
    @Getter
    String code;

    Object[] args;

    @Setter boolean returnCodeAsValue = false;

    public CustomMessage(String code, Object[] args) {
        setValueFromCode(code);
        this.args = args;
    }

    public CustomMessage(String code) {
        setValueFromCode(code);
    }

    public static CustomMessage getCustomMessage(MessageCodes code) {
        return new CustomMessage(code.getValue());
    }

    public static CustomMessage getCustomMessage(MessageCodes code, Object... args) {
        return new CustomMessage(code.getValue(), args);
    }

    public static CustomMessage getCustomMessage(String value, boolean returnCodeAsValue) {
        CustomMessage CustomMessage = new CustomMessage();
        CustomMessage.setCode(value);
        CustomMessage.setReturnCodeAsValue(returnCodeAsValue);
        return CustomMessage;
    }

    private void setValueFromCode(String code) {
        this.code = setTranslateValueFromCode(code);
    }

    private String setTranslateValueFromCode(String code) {
        String message = ApplicationText.get(code, ApplicationText.Language.EN);
        if (message == null || message.trim().isEmpty()) {
            return code;
        } else {
                return message;
        }
    }

    public static String getCustomMessageString(MessageCodes code) {
        return CustomMessage.getCustomMessage(code).getMessage();
    }

    public static String getCustomMessageString(MessageCodes code, Object... args) {
        return CustomMessage.getCustomMessage(code, args).getMessage();
    }

    public String getMessage() {
        if (returnCodeAsValue) {
            return code;
        }
        if (args == null) {
            return code;
        }
        try {
            return String.format(code, args);
        } catch (Exception e) {
            return code;
        }
    }
}
