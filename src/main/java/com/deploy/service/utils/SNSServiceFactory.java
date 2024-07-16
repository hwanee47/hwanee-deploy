package com.deploy.service.utils;

import com.deploy.entity.enums.BuildType;
import com.deploy.entity.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SNSServiceFactory {

    private final SlackService slackService;

    public SNSService getSNSService(NotificationType type) {
        switch (type) {
            case SLACK:
                return slackService;
            case MAIL:
                return null;
            default:
                throw new IllegalArgumentException("알수없는 Notification 유형 입니다. type: " + type);
        }
    }
}
