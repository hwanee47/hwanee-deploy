package com.deploy.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "CREDENTIAL")
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CREDENTIAL_ID")
    private Long id;

    @Column(name = "IDENTIFIER_NAME")
    private String identifierName; // 유저 식별용 이름

    @Column(name = "USERNAME")
    private String username; // 계정

    @Column(name = "PRIVATE_KEY")
    private String privateKey; // 인증키

    @Column(name = "PASSPHRASE")
    private String passphrase; // 인증키 생성시 입력한 비밀번호


}
