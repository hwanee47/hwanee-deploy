package com.deploy.service;

import com.deploy.dto.request.PasswordUpdateReq;
import com.deploy.dto.request.UserCreateReq;
import com.deploy.dto.request.UserUpdateReq;
import com.deploy.dto.response.UserRes;
import com.deploy.entity.User;
import com.deploy.exception.AppBizException;
import com.deploy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * 사용자 조회
     * @param id
     * @return
     */
    public UserRes findUser(Long id) {
        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new AppBizException("No such data in User."));

        return new UserRes(findUser.getUsername(), findUser.getEmail());
    }


    /**
     * 사용자 추가
     * @param userCreateReq
     * @return
     */
    @Transactional
    public Long createUser(UserCreateReq userCreateReq) {

        String email = userCreateReq.getEmail();
        String username = userCreateReq.getUsername();
        String password = userCreateReq.getPassword();
        String encryptedPassword = bCryptPasswordEncoder.encode(password);

        Optional<User> dupEmail = userRepository.findByEmail(userCreateReq.getEmail());

        if (dupEmail.isPresent()) {
            throw new AppBizException("이미 존재하는 이메일입니다.");
        }

        User user = User.createUser(email, encryptedPassword, username);

        userRepository.save(user);

        return user.getId();
    }


    /**
     * 사용자 수정
     * @param userUpdateReq
     * @return
     */
    @Transactional
    public UserRes updateUser(Long id, UserUpdateReq userUpdateReq) {

        String username = userUpdateReq.getUsername();

        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new AppBizException("No such data in User."));


        findUser.changeInfo(username);

        return new UserRes(username, findUser.getEmail());
    }


    /**
     * 비밀번호 변경
     * @param passwordUpdateReq
     */
    @Transactional
    public void changePassword(PasswordUpdateReq passwordUpdateReq) {

        Long userId = passwordUpdateReq.getUserId();
        String currentPassword = passwordUpdateReq.getCurrentPassword();
        String newPassword = passwordUpdateReq.getNewPassword();
        String newPasswordConfirm = passwordUpdateReq.getNewPasswordConfirm();

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppBizException("No such data in User."));


        // Password mismatched.
        if (!bCryptPasswordEncoder.matches(currentPassword, findUser.getPassword())) {
            throw new AppBizException("현재 비밀번호가 일치하지 않습니다.");
        }

        // New Password mismatched.
        if (!newPassword.equals(newPasswordConfirm)) {
            throw new AppBizException("새 비밀번호가 확인과 일치하지 않습니다.");
        }


        // Save password.
        findUser.changePassword(bCryptPasswordEncoder.encode(newPassword));

    }

}
