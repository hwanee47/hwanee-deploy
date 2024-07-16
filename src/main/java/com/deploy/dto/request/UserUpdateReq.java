package com.deploy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateReq {
    @NotBlank(message = "사용자명은 필수입력 값입니다.")
    private String username;
}
