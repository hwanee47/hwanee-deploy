package com.deploy.dto.request;

import com.deploy.entity.enums.ScmType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScmConfigUpdateReq {

    @NotNull( message = "유효하지 않은 유형(type)이 입력되었습니다.")
    private ScmType type;

    private String description;

    @NotEmpty( message = "url은 필수입력입니다.")
    private String url;

    @NotEmpty( message = "username은 필수입력입니다.")
    private String username;

    @NotEmpty( message = "password은 필수입력입니다.")
    private String password;

    @NotEmpty( message = "branch은 필수입력입니다.")
    private String branch;

}


