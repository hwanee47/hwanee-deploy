package com.deploy.repository;

import com.deploy.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void findByEmail() {
        //given
        String email = "hwaneehwanee@gmail.com";
        String password = "1234";
        String encodedPassword = passwordEncoder.encode(password);
        String username = "kimjinhwan";

        User user = User.createUser(email, encodedPassword, username);
        userRepository.save(user);

        log.info("encodedPassword :: {}",encodedPassword);

        //when
        User findUser = userRepository.findByEmail(email+"2")
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을수 없습니다."));

        //then
        assertThat(findUser.getEmail()).isEqualTo(email);

    }

}