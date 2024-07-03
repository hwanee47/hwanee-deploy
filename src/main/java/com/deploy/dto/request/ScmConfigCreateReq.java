package com.deploy.dto.request;

import com.deploy.entity.ScmConfig;
import com.deploy.entity.enums.ScmType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScmConfigCreateReq {

    @NotNull( message = "유효하지 않은 유형(type)이 입력되었습니다.")
    private ScmType type;

    @NotEmpty( message = "url은 필수입력입니다.")
    private String url;

    @NotEmpty( message = "username은 필수입력입니다.")
    private String username;

    @NotEmpty( message = "password은 필수입력입니다.")
    private String password;

    @NotEmpty( message = "branch은 필수입력입니다.")
    private String branch;

    private String description;


    @Builder
    public ScmConfigCreateReq(ScmType type, String description, String url, String username, String password, String branch) {
        this.type = type;
        this.description = description;
        this.url = url;
        this.username = username;
        this.password = password;
        this.branch = branch;
    }

}
