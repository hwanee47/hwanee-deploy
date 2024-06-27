package com.deploy.entity;

import com.deploy.entity.enums.ScmType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "SCM_CONFIG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScmConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCM_ID")
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

    @Column(name = "CLONE_PATH")
    private String clonePath; // 클론경로


    //== 생성 메서드 ==//
    public static ScmConfig createScmConfig(ScmType scmType, String description, String url, String username, String password, String branch) {
        ScmConfig scmConfig = new ScmConfig();
        scmConfig.scmType = scmType;
        scmConfig.description = description;
        scmConfig.url = url;
        scmConfig.username = username;
        scmConfig.password = password;
        scmConfig.branch = branch;

        return scmConfig;
    }

    @Builder
    public ScmConfig(ScmType scmType, String description, String url, String username, String password, String branch) {
        this.scmType = scmType;
        this.description = description;
        this.url = url;
        this.username = username;
        this.password = password;
        this.branch = branch;
    }


    //== 비즈니스 로직 ==//
}
