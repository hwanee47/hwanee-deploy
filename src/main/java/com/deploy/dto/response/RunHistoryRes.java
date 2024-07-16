package com.deploy.dto.response;

import com.deploy.entity.enums.HistoryStatus;
import com.deploy.utils.AppUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RunHistoryRes {

    private Long id;

    private Long seq;

    @JsonProperty(value = "isSuccess")
    private boolean isSuccess;

    private String totalRunTime;

    private LocalDateTime createdAt;

    private HistoryStatus status;


    @Builder
    public RunHistoryRes(Long id, Long seq, boolean isSuccess, Long totalRunTime, LocalDateTime createdAt, HistoryStatus status) {
        this.id = id;
        this.seq = seq;
        this.isSuccess = isSuccess;
        this.totalRunTime = AppUtils.convertToRunTime(totalRunTime);
        this.createdAt = createdAt.withNano(0);
        this.status = status;
    }


}
