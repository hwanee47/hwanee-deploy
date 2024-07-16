package com.deploy.dto.request;

import com.deploy.entity.enums.ScmType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecuteConntectReq {

    @NotNull( message = "유효하지 않은 유형(type)이 입력되었습니다.")
    private ScmType type;

    @NotEmpty( message = "url은 필수입력입니다.")
    private String url;

    @NotEmpty( message = "계정은 필수입력입니다.")
    private String username;

    @NotEmpty( message = "비밀번호는 필수입력입니다.")
    private String password;

    @NotEmpty( message = "브랜치는 필수입력입니다.")
    private String branch;
}
