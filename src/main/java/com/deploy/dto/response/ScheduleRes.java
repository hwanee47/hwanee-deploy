package com.deploy.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleRes {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime scheduleTime;
    private LocalDateTime executedTime;
    private String description;

    @Builder
    public ScheduleRes(Long id, LocalDateTime createdAt, LocalDateTime scheduleTime, LocalDateTime executedTime, String description) {
        this.id = id;
        this.createdAt = createdAt;
        this.scheduleTime = scheduleTime;
        this.executedTime = executedTime;
        this.description = description;
    }
}
