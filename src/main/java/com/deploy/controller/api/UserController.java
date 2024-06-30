package com.deploy.controller.api;

import com.deploy.dto.request.PasswordUpdateReq;
import com.deploy.dto.response.handler.ResponseHandler;
import com.deploy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordUpdateReq passwordUpdateReq) {

        userService.changePassword(passwordUpdateReq);

        return ResponseHandler.generateResponse(HttpStatus.OK, "success", "");
    }
}
