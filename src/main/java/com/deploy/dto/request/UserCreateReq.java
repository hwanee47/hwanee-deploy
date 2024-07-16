package com.deploy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateReq {

    @NotBlank(message = "이메일은 필수입력 값입니다.")
    private String email;

    @NotBlank(message = "사용자명은 필수입력 값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입력 값입니다.")
    private String password;

}
