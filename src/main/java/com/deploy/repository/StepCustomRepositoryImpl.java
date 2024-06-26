package com.deploy.repository;

import com.deploy.dto.request.JobSearchCond;
import com.deploy.entity.Job;
import com.deploy.entity.QJob;
import com.deploy.entity.QStep;
import com.deploy.entity.Step;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.deploy.entity.QStep.step;

public class StepCustomRepositoryImpl implements StepCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public StepCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Long maxStepIndex(Long jobId) {
        Long maxValue = jpaQueryFactory
                .select(step.stepIndex.max())
                .from(step)
                .where(step.job.id.eq(jobId))
                .fetchOne();

        return maxValue == null ? 0L : maxValue;
    }
}
