package com.deploy.entity;

import com.deploy.entity.enums.ScmType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "SCM_CONFIG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScmConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCM_ID")
    private Long id;

    @Comment("SCM유형 : svn, git")
    @Enumerated(EnumType.STRING)
    @Column(name = "SCM_TYPE")
    private ScmType scmType;

    @Comment("설명")
    @Column(name = "DESCRIPTION")
    private String description;

    @Comment("Repository URL")
    @Column(name = "URL")
    private String url;

    @Comment("사용자명")
    @Column(name = "USERNAME")
    private String username;

    @Comment("비밀번호")
    @Column(name = "PASSWORD")
    private String password;

    @Comment("브랜치명")
    @Column(name = "BRANCH")
    private String branch;

    @Comment("클론경로")
    @Column(name = "CLONE_PATH")
    private String clonePath;

    @Comment("연결성공여부")
    @Column(name = "IS_CONNECTED")
    private boolean isConnected;

    @Comment("실패 메세지")
    @Column(name = "FAIL_MESSAGE")
    private String failMessage;

    //== 생성 메서드 ==//
    public static ScmConfig createScmConfig(ScmType scmType, String description, String url, String username, String password, String branch) {
        ScmConfig scmConfig = new ScmConfig();
        scmConfig.scmType = scmType;
        scmConfig.description = description;
        scmConfig.url = url;
        scmConfig.username = username;
        scmConfig.password = password;
        scmConfig.branch = branch;
        scmConfig.isConnected = false;

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
    public void changeInfo(ScmType scmType, String description, String url, String username, String password, String branch) {
        this.scmType = scmType;
        this.description = description;
        this.url = url;
        this.username = username;
        this.password = password;
        this.branch = branch;
    }

    // 연결성공
    public void connectSuccess() {
        this.isConnected = true;
        this.failMessage = null;
    }

    // 연결실패
    public void connectFail(String failMessage) {
        this.isConnected = false;
        this.failMessage = failMessage;
    }
}
