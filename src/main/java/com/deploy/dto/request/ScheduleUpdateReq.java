package com.deploy.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleUpdateReq {

    @NotNull(message = "스케줄 시간은 필수값입니다.")
    private LocalDateTime time;
    
    private String description;

    @Builder
    public ScheduleUpdateReq(LocalDateTime time, String description) {
        this.time = time;
        this.description = description;
    }
}
