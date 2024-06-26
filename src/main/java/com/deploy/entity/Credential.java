package com.deploy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "CREDENTIAL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CREDENTIAL_ID")
    private Long id;

    @Column(name = "IDENTIFIER_NAME")
    private String identifierName; // 유저 식별용 이름

    @Column(name = "TARGET_USERNAME")
    private String targetUsername; // 계정

    @Column(name = "TARGET_PASSWORD")
    private String targetPassword; // 비밀번호

    @Column(name = "TARGET_HOST")
    private String targetHost; // 원격지 호스트

    @Column(name = "TARGET_PORT")
    private int targetPort; // 원격지 포트

    @Column(name = "PRIVATE_KEY")
    private String privateKey; // 인증키

    @Column(name = "PASSPHRASE")
    private String passphrase; // 인증키 비밀번호


    //== 생성 메서드 ==//
    @Builder
    public Credential(String identifierName, String targetUsername, String targetPassword, String targetHost, int targetPort, String privateKey, String passphrase) {
        this.identifierName = identifierName;
        this.targetUsername = targetUsername;
        this.targetPassword = targetPassword;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
        this.privateKey = privateKey;
        this.passphrase = passphrase;
    }

    //== 비즈니스 로직 ==//
    public void encrptPassphase() {

    }


}
