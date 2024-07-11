package com.deploy.service.event;

import lombok.Getter;

@Getter
public class RunCompletedEvent {

    private Long runHistoryId;

    public RunCompletedEvent(Long runHistoryId) {
        this.runHistoryId = runHistoryId;
    }
}
