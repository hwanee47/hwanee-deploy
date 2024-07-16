package com.deploy.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobNotificationUpdateReq {

    @NotNull(message = "Notification id는 필수값입니다.")
    private Long notificationId;
}
