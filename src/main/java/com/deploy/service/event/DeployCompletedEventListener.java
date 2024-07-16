package com.deploy.service.event;

import com.deploy.entity.RunHistory;
import com.deploy.exception.AppBizException;
import com.deploy.repository.RunHistoryRepository;
import com.deploy.service.NotificationService;
import com.deploy.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.deploy.exception.AppErrorCode.NOT_FOUND_ENTITY_IN_RUNHISTORY;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeployCompletedEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void sendMessage(DeployCompletedEvent event) {

        StringBuilder message = new StringBuilder();
        message.append("<< 선택배포 작업완료 알림 >>");
        message.append("\n- 처리결과 : " + event.isSuccess());
        message.append("\n- 배포된 파일 : " + event.getBuildFileName());

        notificationService.sendMessage(event.getNotificationId(), message.toString());
    }
}
