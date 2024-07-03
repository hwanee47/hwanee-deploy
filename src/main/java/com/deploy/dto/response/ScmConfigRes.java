package com.deploy.dto.response;

import com.deploy.entity.ScmConfig;
import com.deploy.entity.enums.ScmType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScmConfigRes {

    private Long id;
    private ScmType type;
    private String description;
    private String url;
    private String username;
    private String password;
    private String branch;
    private String clonePath;

    @JsonProperty(value = "isConnected")
    private boolean isConnected;
    private String failMessage;


    @Builder
    public ScmConfigRes(Long id, ScmType type, String description, String url, String username, String password, String branch, String clonePath) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.url = url;
        this.username = username;
        this.password = password;
        this.branch = branch;
        this.clonePath = clonePath;
    }

    public ScmConfigRes(ScmConfig scmConfig) {
        this.id = scmConfig.getId();
        this.type = scmConfig.getScmType();
        this.description = scmConfig.getDescription();
        this.url = scmConfig.getUrl();
        this.username = scmConfig.getUsername();
        this.password = scmConfig.getPassword();
        this.branch = scmConfig.getBranch();
        this.clonePath = scmConfig.getClonePath();
        this.isConnected = scmConfig.isConnected();
        this.failMessage = scmConfig.getFailMessage();
    }
}
