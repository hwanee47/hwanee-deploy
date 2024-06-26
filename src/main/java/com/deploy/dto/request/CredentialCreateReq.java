package com.deploy.dto.request;

import com.deploy.entity.Credential;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialCreateReq {

    @NotEmpty(message = "name은 필수값입니다.")
    private String name; // 유저 식별용 이름

    private String username; // 계정

    private String privateKey; // 인증키

    private String passphrase; // 인증키 비밀번호

    public Credential toEntity() {
        return null;
    }

}
