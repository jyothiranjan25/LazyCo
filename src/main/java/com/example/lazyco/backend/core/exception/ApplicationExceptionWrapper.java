package com.example.lazyco.backend.core.exception;

import com.example.lazyco.backend.core.messages.CustomMessage;
import org.springframework.http.HttpStatusCode;

public class ApplicationExceptionWrapper extends  RuntimeException {
    private HttpStatusCode httpStatusCode;
    private Exception exception;
    private CustomMessage customMessage;
}