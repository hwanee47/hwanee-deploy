package com.deploy.controller.api;

import com.deploy.dto.request.PasswordUpdateReq;
import com.deploy.dto.response.ResUser;
import com.deploy.dto.response.handler.ResponseHandler;
import com.deploy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findUser(@PathVariable Long id) {
        ResUser resUser = userService.findUser(id);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", resUser);
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordUpdateReq passwordUpdateReq) {

        userService.changePassword(passwordUpdateReq);

        return ResponseHandler.generateResponse(HttpStatus.OK, "success", "");
    }
}
