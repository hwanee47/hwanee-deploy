package com.deploy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "CODE_MANAGE_SET")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeManageSet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODE_MANAGE_SET_ID")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "SCM_TYPE")
    private ScmType scmType; // SCM유형 : svn, git

    @Column(name = "DESCRIPTION")
    private String description; // 설명

    @Column(name = "URL")
    private String url; // Repository URL

    @Column(name = "USERNAME")
    private String username; // 사용자명

    @Column(name = "PASSWORD")
    private String password; // 비밀번호

    @Column(name = "BRANCH")
    private String branch; // 브랜치명

    @Column(name = "IS_CREDENTIAL")
    private boolean isCredential;   // 자격증명여부

    @Column(name = "CREDENTIAL_FAIL_LOG")
    private String credentialFailLog;   // 자격증명실패로그


}
