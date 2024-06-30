package com.deploy.config.security;

import com.deploy.dto.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("login success.");

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        request.getSession().setAttribute("userId", customUserDetails.getId());
        request.getSession().setAttribute("username", customUserDetails.getUsername());

        response.sendRedirect("/");

    }

}
