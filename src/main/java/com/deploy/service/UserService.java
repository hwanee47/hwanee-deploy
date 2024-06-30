package com.deploy.service;

import com.deploy.dto.request.PasswordUpdateReq;
import com.deploy.entity.User;
import com.deploy.exception.AppBizException;
import com.deploy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
