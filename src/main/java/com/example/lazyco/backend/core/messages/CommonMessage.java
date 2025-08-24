package com.example.lazyco.backend.core.messages;

import lombok.Getter;

@Getter
public enum CommonMessage implements MessageCodes{

    APPLICATION_ERROR("COMMON_MODULES.APPLICATION_ERROR"),

    ;private final String value;

    CommonMessage(String value) {
        this.value = value;
    }
}
