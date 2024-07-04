package com.deploy.dto.request;

import com.deploy.entity.enums.BuildType;
import com.deploy.entity.enums.StepType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StepUpdateReq {

    @NotNull(message = "유효하지 않은 유형(type)이 입력되었습니다.")
    private StepType type;

    private BuildType buildType;

    private Long credentialId;
    private Long scmConfigId;

    private String command;

}
