package com.deploy.service;

import com.deploy.dto.request.PasswordUpdateReq;
import com.deploy.entity.User;
import com.deploy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @BeforeEach
    public void init() {
        User user = User.builder()
                .username("test")
                .email("hwaneehwanee@gmail.com")
                .password(bCryptPasswordEncoder.encode("1234"))
                .build();

        System.out.println(user.getPassword());

        userRepository.save(user);
    }


    @Test
    public void 패스워드변경_테스트() {
        //given
        User user = userRepository.findByEmail("hwaneehwanee@gmail.com").get();

        PasswordUpdateReq request = new PasswordUpdateReq();
        request.setUserId(user.getId());
        request.setCurrentPassword("1234");
        request.setNewPassword("1111");
        request.setNewPasswordConfirm("1111");

        //when
        userService.changePassword(request);
        User findUser = userRepository.findById(user.getId()).get();

        //then
        assertThat(bCryptPasswordEncoder.matches(request.getNewPassword(), findUser.getPassword())).isTrue();

    }

    @Test
    public void 패스워드변경시_현재비밀번호와맞지않으면_에러() {
        //given
        User user = userRepository.findByEmail("hwaneehwanee@gmail.com").get();

        PasswordUpdateReq request = new PasswordUpdateReq();
        request.setUserId(user.getId());
        request.setCurrentPassword("fail");

        //when

        //then
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.changePassword(request),
                "현재 비밀번호와 일치하지않으면 에러.");

    }

    @Test
    public void 패스워드변경시_새비밀번호가일치하지않으면_에러() {
        //given
        User user = userRepository.findByEmail("hwaneehwanee@gmail.com").get();

        PasswordUpdateReq request = new PasswordUpdateReq();
        request.setUserId(user.getId());
        request.setCurrentPassword("1234");
        request.setNewPassword("1111");
        request.setNewPasswordConfirm("2222");

        //when

        //then
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.changePassword(request),
                "새 비밀번호와 확인이 일치하지않으면 에러.");


    }
}