package com.deploy.dto.request;

import com.deploy.entity.Job;
import com.deploy.entity.Step;
import com.deploy.entity.enums.BuildType;
import com.deploy.entity.enums.ScmType;
import com.deploy.entity.enums.StepType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StepCreateReq {

    @NotNull(message = "jobId는 필수값 입니다.")
    private Long jobId;

    @NotNull(message = "유효하지 않은 유형(type)이 입력되었습니다.")
    private StepType type;

    private BuildType buildType;

    private Long credentialId;
    private Long scmConfigId;

    private String command;

    @JsonProperty(value = "isTest")
    private boolean isTest;


    @Builder
    public StepCreateReq(Long jobId, StepType type, BuildType buildType, Long credentialId, Long scmConfigId, String command) {
        this.jobId = jobId;
        this.type = type;
        this.buildType = buildType;
        this.credentialId = credentialId;
        this.scmConfigId = scmConfigId;
        this.command = command;
    }
}
