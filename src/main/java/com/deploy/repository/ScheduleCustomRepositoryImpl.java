package com.deploy.repository;

import com.deploy.entity.QSchedule;
import com.deploy.entity.RunHistory;
import com.deploy.entity.RunHistoryDetail;
import com.deploy.entity.Schedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.deploy.entity.QRunHistory.runHistory;
import static com.deploy.entity.QRunHistoryDetail.runHistoryDetail;
import static com.deploy.entity.QSchedule.schedule;

public class ScheduleCustomRepositoryImpl implements ScheduleCustomRepository  {

    private final JPAQueryFactory jpaQueryFactory;

    public ScheduleCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Schedule> searchByJobId(Long jobId, Pageable pageable) {

        // contents
        List<Schedule> contents = jpaQueryFactory
                .selectFrom(schedule)
                .where(schedule.job.id.eq(jobId))
                .orderBy(schedule.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // total count
        Long totalCount = jpaQueryFactory
                .select(schedule.count())
                .from(schedule)
                .fetchOne();


        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(contents, pageable, totalCount);
    }




}
