package com.deploy.repository;

import com.deploy.entity.RunHistory;
import com.deploy.entity.RunHistoryDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.deploy.entity.QRunHistory.runHistory;
import static com.deploy.entity.QRunHistoryDetail.runHistoryDetail;

public class RunHistoryCustomRepositoryImpl implements RunHistoryCustomRepository  {

    private final JPAQueryFactory jpaQueryFactory;

    public RunHistoryCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<RunHistory> searchByJobId(Long jobId, Pageable pageable) {

        // contents
        List<RunHistory> contents = jpaQueryFactory
                .selectFrom(runHistory)
                .where(runHistory.job.id.eq(jobId))
                .orderBy(runHistory.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // total count
        Long totalCount = jpaQueryFactory
                .select(runHistory.count())
                .from(runHistory)
                .fetchOne();


        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(contents, pageable, totalCount);
    }


    @Override
    public Page<RunHistoryDetail> searchByHistoryId(Long jobId, Long historyId, Pageable pageable) {

        // contents
        List<RunHistoryDetail> contents = jpaQueryFactory
                .selectFrom(runHistoryDetail)
                .where(
                        runHistoryDetail.runHistory.job.id.eq(jobId)
                                .and(runHistoryDetail.runHistory.id.eq(historyId))
                )
                .orderBy(runHistoryDetail.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // total count
        Long totalCount = jpaQueryFactory
                .select(runHistoryDetail.count())
                .from(runHistoryDetail)
                .fetchOne();


        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(contents, pageable, totalCount);
    }


}
