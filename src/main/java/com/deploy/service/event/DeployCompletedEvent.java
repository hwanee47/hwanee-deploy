package com.deploy.service.event;

import lombok.Getter;

@Getter
public class DeployCompletedEvent {

    private boolean isSuccess;
    private String buildFileName;
    private Long notificationId;

    public DeployCompletedEvent(Long notificationId, boolean isSuccess, String buildFileName) {
        this.notificationId = notificationId;
        this.isSuccess = isSuccess;
        this.buildFileName = buildFileName;
    }
}
