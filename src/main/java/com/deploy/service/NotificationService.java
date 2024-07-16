package com.deploy.service;

import com.deploy.dto.request.NotificationCreateReq;
import com.deploy.dto.request.NotificationSearchCond;
import com.deploy.dto.request.NotificationUpdateReq;
import com.deploy.dto.response.NotificationRes;
import com.deploy.entity.Notification;
import com.deploy.entity.enums.NotificationType;
import com.deploy.exception.AppBizException;
import com.deploy.exception.AppErrorCode;
import com.deploy.repository.NotificationRepository;
import com.deploy.service.utils.AesService;
import com.deploy.service.utils.SNSService;
import com.deploy.service.utils.SNSServiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.deploy.exception.AppErrorCode.NOT_FOUND_ENTITY_IN_NOTIFICATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AesService aesService;
    private final SNSServiceFactory snsServiceFactory;


    /**
     * Notification 다건 조회
     * @param condition
     * @param pageable
     * @return
     */
    public Page<NotificationRes> search(NotificationSearchCond condition, Pageable pageable) {
        Page<Notification> list = notificationRepository.search(condition, pageable);

        Page<NotificationRes> results = new PageImpl<>(
                list.stream().map(Notification -> NotificationRes.builder()
                        .id(Notification.getId())
                        .type(Notification.getType())
                        .url(Notification.getUrl())
                        .apiKey(aesService.decrypt(Notification.getApiKey()))
                        .build()).collect(Collectors.toList())
                , pageable
                , list.getTotalElements()
        );

        return results;
    }

    /**
     * Crendential 전체 조회
     * @return
     */
    public List<NotificationRes> searchAll() {
        List<Notification> list = notificationRepository.findAll();

        List<NotificationRes> results = list.stream()
                .map(Notification -> NotificationRes.builder()
                        .id(Notification.getId())
                        .type(Notification.getType())
                        .url(Notification.getUrl())
                        .apiKey(aesService.decrypt(Notification.getApiKey()))
                        .build()
                )
                .collect(Collectors.toList());

        return results;
    }

    /**
     * Notification 조회
     * @param id
     * @return
     */
    public NotificationRes findNotification(Long id) {

        Notification findNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_NOTIFICATION));

        return NotificationRes.builder()
                .id(findNotification.getId())
                .type(findNotification.getType())
                .url(findNotification.getUrl())
                .apiKey(aesService.decrypt(findNotification.getApiKey()))
                .build();

    }

    /**
     * Notification 추가
     * @param request
     * @return
     */
    @Transactional
    public Long createNotification(NotificationCreateReq request) {

        NotificationType type = request.getType();
        String url = request.getUrl();
        String apiKey = request.getApiKey();

        Notification notification = Notification.createNotification(
                type, url, aesService.encrypt(apiKey));

        // save
        notificationRepository.save(notification);

        return notification.getId();
    }


    /**
     * Notification 수정
     * @param id
     * @param request
     * @return
     */
    @Transactional
    public Long updateNotification(Long id, NotificationUpdateReq request) {

        NotificationType type = request.getType();
        String url = request.getUrl();
        String apiKey = request.getApiKey();

        Notification findNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_NOTIFICATION));


        // update
        findNotification.changeInfo(type, url, aesService.encrypt(apiKey));


        return id;
    }


    /**
     * Notification 삭제
     * @param id
     */
    @Transactional
    public void deleteNotification(Long id) {
        Notification findNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_NOTIFICATION));

        notificationRepository.delete(findNotification);
    }


    /**
     * Send Message
     * @param notificationId
     * @param message
     */
    @Async
    public void sendMessage(Long notificationId, String message) {
        Notification findNotification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_NOTIFICATION));

        SNSService snsService = snsServiceFactory.getSNSService(findNotification.getType());

        snsService.sendMessage(findNotification.getUrl(), message);
    }
}
