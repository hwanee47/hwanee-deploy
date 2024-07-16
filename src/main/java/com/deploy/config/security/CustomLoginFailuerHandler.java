package com.deploy.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class CustomLoginFailuerHandler implements AuthenticationFailureHandler {

    private final String LOGIN_URL;

    public CustomLoginFailuerHandler(String loginUrl) {
        this.LOGIN_URL = loginUrl;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.info("login fail. message={}",exception.getMessage());
        request.getSession().setAttribute("error_message", "Invaild email or password.");
        response.sendRedirect(LOGIN_URL);

    }
}
