package com.deploy.dto.response;

import com.deploy.entity.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRes {

    private Long id;
    private NotificationType type;
    private String url;
    private String apiKey;

    @Builder
    public NotificationRes(Long id, NotificationType type, String url, String apiKey) {
        this.id = id;
        this.type = type;
        this.url = url;
        this.apiKey = apiKey;
    }
}
