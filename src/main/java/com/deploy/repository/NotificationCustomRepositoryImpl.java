package com.deploy.repository;

import com.deploy.dto.request.NotificationSearchCond;
import com.deploy.entity.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.deploy.entity.QNotification.notification;

public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public NotificationCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Notification> search(NotificationSearchCond condition, Pageable pageable) {

        // contents
        List<Notification> contents = jpaQueryFactory
                .selectFrom(notification)
                .orderBy(notification.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        // total count
        Long totalCount = jpaQueryFactory
                .select(notification.count())
                .from(notification)
                .fetchOne();


        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(contents, pageable, totalCount);
    }
}
