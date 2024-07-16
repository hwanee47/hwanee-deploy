package com.deploy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PasswordUpdateReq {

    @NotNull(message = "userId는 필수입력 값입니다.")
    private Long userId;

    @NotBlank(message = "현재 비밀번호는 필수입력 값입니다.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호는 필수입력 값입니다.")
    private String newPassword;

    @NotBlank(message = "새 비밀번호(확인)는 필수입력 값입니다.")
    private String newPasswordConfirm;


}
