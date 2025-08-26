package com.example.lazyco.backend.core.Utils;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Exceptions.SimpleResponseDTO;
import com.example.lazyco.backend.core.GosnConf.GsonSingleton;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Utility class for creating standardized HTTP responses Supports various response types including
 * DTOs, Files, JSONObject, etc.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtils {

    public static <T extends AbstractDTO<T>> ResponseEntity<T> sendResponse(T abstractDTO) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(abstractDTO);
    }

    public static <T extends AbstractDTO<T>> ResponseEntity<T> sendResponseByCode(int code,T abstractDTO) {
        return ResponseEntity.status(code).contentType(MediaType.APPLICATION_JSON).body(abstractDTO);
    }
}
