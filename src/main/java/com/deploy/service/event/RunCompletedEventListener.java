package com.deploy.service.event;

import com.deploy.dto.response.RunHistoryRes;
import com.deploy.entity.RunHistory;
import com.deploy.exception.AppBizException;
import com.deploy.exception.AppErrorCode;
import com.deploy.repository.RunHistoryRepository;
import com.deploy.service.NotificationService;
import com.deploy.service.RunHistoryService;
import com.deploy.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.deploy.exception.AppErrorCode.NOT_FOUND_ENTITY_IN_RUNHISTORY;

@Slf4j
@Component
@RequiredArgsConstructor
public class RunCompletedEventListener {

    private final NotificationService notificationService;
    private final RunHistoryRepository runHistoryRepository;

    @EventListener
    public void sendMessage(RunCompletedEvent event) {

        Long runHistoryId = event.getRunHistoryId();

        RunHistory findRunHistory = runHistoryRepository.findById(runHistoryId)
                    .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_RUNHISTORY));


        StringBuilder message = new StringBuilder();
        message.append("<< Run NOW 작업 완료 알림 >>");
        message.append("\n- 처리상태 : " + findRunHistory.isSuccess());
        message.append("\n- 소요시간 : " + AppUtils.convertToRunTime(findRunHistory.getTotalRunTime()));
        message.append("\n- 로그파일 : " + findRunHistory.getLogFilePath());

        notificationService.sendMessage(findRunHistory.getJob().getNotification().getId(), message.toString());
    }
}
