package com.deploy.controller.api;

import com.deploy.dto.response.handler.ResponseHandler;
import com.google.common.collect.ImmutableMap;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class SessionController {

    @GetMapping("/get-session-info")
    public ResponseEntity<?> getSessionInfo(HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("userId");
        String username = (String) httpSession.getAttribute("username");

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "success",
                ImmutableMap.builder()
                        .put("userId", userId)
                        .put("username", username)
                        .build()
        );
    }
}
