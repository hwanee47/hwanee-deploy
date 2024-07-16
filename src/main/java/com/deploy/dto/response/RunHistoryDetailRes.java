package com.deploy.dto.response;

import com.deploy.entity.enums.HistoryStatus;
import com.deploy.entity.enums.StepType;
import com.deploy.utils.AppUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RunHistoryDetailRes {

    private Long id;

    private Long historyId;

    @JsonProperty(value = "isSuccess")
    private boolean isSuccess;

    private String runTime;

    private String runFailLog;

    private StepType stepType;

    private HistoryStatus status;


    @Builder
    public RunHistoryDetailRes(Long id, Long historyId, boolean isSuccess, Long runTime, String runFailLog, StepType stepType, HistoryStatus status) {
        this.id = id;
        this.historyId = historyId;
        this.isSuccess = isSuccess;
        this.runTime = AppUtils.convertToRunTime(runTime);
        this.runFailLog = runFailLog;
        this.stepType = stepType;
        this.status = status;
    }


}
