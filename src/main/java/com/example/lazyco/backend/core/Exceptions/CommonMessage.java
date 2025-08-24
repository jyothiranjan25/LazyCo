package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum CommonMessage implements MessageCodes {

    CUSTOM_MESSAGE("COMMON_MODULES.CUSTOM_MESSAGE"),
    APPLICATION_ERROR("COMMON_MODULES.APPLICATION_ERROR"),

    ;private final String value;

    CommonMessage(String value) {
        this.value = value;
    }
}
