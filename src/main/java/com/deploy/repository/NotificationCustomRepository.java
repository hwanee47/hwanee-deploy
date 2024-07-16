package com.deploy.repository;

import com.deploy.dto.request.CredentialSearchCond;
import com.deploy.dto.request.NotificationSearchCond;
import com.deploy.entity.Credential;
import com.deploy.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationCustomRepository {
    Page<Notification> search(NotificationSearchCond condition, Pageable pageable);
}
