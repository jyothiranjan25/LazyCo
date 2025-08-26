package com.example.lazyco.backend.core.AbstractClasses.Controller.ControllerComponents;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import org.springframework.http.ResponseEntity;

import static com.example.lazyco.backend.core.Utils.ResponseUtils.*;

public abstract class ControllerTemplate <D extends AbstractDTO<D>> {

    public ResponseEntity<D> template(D t) {
        t = execute(t);
        if (isPostRequest()) {
            return sendResponseByCode(201,t);
        } else {
            return sendResponse(t);
        }
    }

    abstract D execute(D t);

    protected boolean isPostRequest() {
        return false;
    }
}