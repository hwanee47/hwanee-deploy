package com.deploy.dto.request;

import com.deploy.entity.Credential;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialCreateReq {

    @NotBlank(message = "식별이름은 필수값입니다.")
    private String name; // 유저 식별용 이름

    @NotBlank(message = "계정명은 필수값입니다.")
    private String username; // 계정

    private String password; // 비밀번호

    private String host; // 호스트

    private Integer port; // 포트

    private String privateKey; // 인증키

    private String passphrase; // 인증키 비밀번호

    private String description; // 비고


    @Builder
    public CredentialCreateReq(String name, String username, String password, String host, Integer port, String privateKey, String passphrase, String description) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.privateKey = privateKey;
        this.passphrase = passphrase;
        this.description = description;
    }
}
