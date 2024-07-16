package com.deploy.dto.request;

import com.deploy.entity.Job;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobCreateReq {

    @NotEmpty(message = "이름(프로젝트명)은 필수값입니다.")
    private String name;
    private String description;


    @Builder
    public JobCreateReq(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
