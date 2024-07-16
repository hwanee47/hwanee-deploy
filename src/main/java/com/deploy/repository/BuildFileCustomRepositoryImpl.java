package com.deploy.repository;

import com.deploy.entity.BuildFile;
import com.deploy.entity.QBuildFile;
import com.deploy.entity.RunHistory;
import com.deploy.entity.RunHistoryDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.deploy.entity.QBuildFile.buildFile;
import static com.deploy.entity.QRunHistory.runHistory;
import static com.deploy.entity.QRunHistoryDetail.runHistoryDetail;

public class BuildFileCustomRepositoryImpl implements BuildFileCustomRepository  {

    private final JPAQueryFactory jpaQueryFactory;

    public BuildFileCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BuildFile> searchByJobId(Long jobId, Pageable pageable) {


        // contents
        List<BuildFile> contents = jpaQueryFactory
                .selectFrom(buildFile)
                .where(buildFile.job.id.eq(jobId))
                .orderBy(buildFile.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // total count
        Long totalCount = jpaQueryFactory
                .select(buildFile.count())
                .from(buildFile)
                .fetchOne();


        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(contents, pageable, totalCount);
    }



}
