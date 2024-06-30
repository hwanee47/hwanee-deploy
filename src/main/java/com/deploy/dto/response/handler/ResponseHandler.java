package com.deploy.dto.response.handler;

import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    /**
     * HTTP 응답 객체 생성
     *
     * @author hwanee
     **/
    public static ResponseEntity<?> generateResponse(HttpStatus status, String message, Object responseData) {
        return ResponseEntity.ok(
                ImmutableMap.builder()
                        .put("status", status.value())
                        .put("message", message)
                        .put("result", responseData)
                        .build()
        );
    }
}
