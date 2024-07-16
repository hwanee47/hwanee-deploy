package com.deploy.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleCreateReq {

    @NotNull(message = "프로젝트 아이디는 필수값입니다.")
    private Long jobId;

    @NotNull(message = "스케줄 시간은 필수값입니다.")
    private LocalDateTime time;

    private String description;

    @Builder
    public ScheduleCreateReq(Long jobId, LocalDateTime time, String description) {
        this.jobId = jobId;
        this.time = time;
        this.description = description;
    }
}
