package com.deploy.controller.api;

import com.deploy.dto.request.PasswordUpdateReq;
import com.deploy.dto.request.UserCreateReq;
import com.deploy.dto.request.UserUpdateReq;
import com.deploy.dto.response.UserRes;
import com.deploy.dto.response.handler.ResponseHandler;
import com.deploy.service.LoginService;
import com.deploy.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LoginService loginService; // 세션업데이트용

    @GetMapping("/{id}")
    public ResponseEntity<?> findUser(@PathVariable Long id) {
        UserRes userRes = userService.findUser(id);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", userRes);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateReq userCreateReq) {
        Long userId = userService.createUser(userCreateReq);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateReq userUpdateReq, HttpSession session) {
        UserRes userRes = userService.updateUser(id, userUpdateReq);

        loginService.updateSessionInfo(userRes.getEmail(), session);

        return ResponseHandler.generateResponse(HttpStatus.OK, "success", userRes);
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordUpdateReq passwordUpdateReq) {
        userService.changePassword(passwordUpdateReq);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", "");
    }

}
