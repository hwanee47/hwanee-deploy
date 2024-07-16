package com.deploy.repository;

import com.deploy.dto.request.JobSearchCond;
import com.deploy.entity.Job;
import com.deploy.entity.QJob;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class JobCustomRepositoryImpl implements JobCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public JobCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Job> search(JobSearchCond condition, Pageable pageable) {

        QJob job = QJob.job;

        // contents
        List<Job> contents = jpaQueryFactory
                .selectFrom(job)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        // total count
        Long totalCount = jpaQueryFactory
                .select(job.count())
                .from(job)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchOne();


        return new PageImpl<>(contents, pageable, totalCount);
    }
}
