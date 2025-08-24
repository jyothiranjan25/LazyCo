package com.example.lazyco.backend.core.exception;

import com.example.lazyco.backend.core.abstracts.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleResponseDTO extends AbstractDTO<SimpleResponseDTO> {
    private String message;

    @Override
    public String toString() {
        return "SimpleResponseDTO [message=" + message + "]";
    }
}