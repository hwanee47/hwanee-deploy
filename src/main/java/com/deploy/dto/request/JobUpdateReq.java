package com.deploy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobUpdateReq {

    @NotBlank(message = "이름은 필수값입니다.")
    private String name;

    private String description;
}
