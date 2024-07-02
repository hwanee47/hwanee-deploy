package com.deploy.dto.response;

import com.deploy.entity.Credential;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CredentialRes {

    private Long id;

    private String name; // 유저 식별용 이름

    private String username; // 계정

    private String password; // 비밀번호

    private String host; // 호스트

    private Integer port; // 포트

    private String privateKey; // 인증키

    private String passphrase; // 인증키 비밀번호

    private String description; // 비고

    private LocalDateTime testConnectedAt;

    @Builder
    public CredentialRes(String name, String username, String password, String host, Integer port, String privateKey, String passphrase, String description) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.privateKey = privateKey;
        this.passphrase = passphrase;
        this.description = description;
    }

    public CredentialRes(Credential credential) {
        this.id = credential.getId();
        this.name = credential.getIdentifierName();
        this.username = credential.getTargetUsername();
        this.password = credential.getTargetPassword();
        this.host = credential.getTargetHost();
        this.port = credential.getTargetPort();
        this.privateKey = credential.getPrivateKey();
        this.passphrase = credential.getPassphrase();
        this.description = credential.getDescription();
    }
}
