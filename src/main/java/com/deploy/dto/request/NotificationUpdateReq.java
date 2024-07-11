package com.deploy.dto.request;

import com.deploy.entity.enums.NotificationType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationUpdateReq {

    @NotNull( message = "유효하지 않은 유형(type)이 입력되었습니다.")
    private NotificationType type;

    @NotEmpty(message = "url은 필수값입니다.")
    private String url;

    private String apiKey;
}
