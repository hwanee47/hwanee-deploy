package com.deploy.service;

import com.deploy.dto.CustomUserDetails;
import com.deploy.entity.User;
import com.deploy.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 이메일입니다. [" + email + "]"));

        return CustomUserDetails.createCustomUserDetails(user);
    }


    public void updateSessionInfo(String email, HttpSession session) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {

            UserDetails updatedUserDetails = loadUserByUsername(email);
            UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                    updatedUserDetails, authentication.getCredentials(), updatedUserDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(newAuth);


            session.setAttribute("username", updatedUserDetails.getUsername());
        }
    }
}
