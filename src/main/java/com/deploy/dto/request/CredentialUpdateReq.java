package com.deploy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialUpdateReq {

    @NotBlank(message = "이름은 필수값입니다.")
    private String name; // 유저 식별용 이름

    @NotBlank(message = "계정명은 필수값입니다.")
    private String username; // 계정

    private String password; // 비밀번호

    private String host; // 호스트

    private Integer port; // 포트

    private String privateKey; // 인증키

    private String passphrase; // 인증키 비밀번호

    private String description; // 비고
}
