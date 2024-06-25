package com.deploy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "CODE_MANAGE_SET")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeManageConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODE_MANAGE_SET_ID")
    private Long id;

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


    //== 생성 메서드 ==//
    @Builder
    public CodeManageConfig(ScmType scmType, String description, String url, String username, String password, String branch) {
        this.scmType = scmType;
        this.description = description;
        this.url = url;
        this.username = username;
        this.password = password;
        this.branch = branch;
    }


    //== 비즈니스 로직 ==//
}
