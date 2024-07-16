package com.deploy.repository;

import com.deploy.dto.request.ScmConfigSearchCond;
import com.deploy.entity.Credential;
import com.deploy.entity.QCredential;
import com.deploy.entity.QScmConfig;
import com.deploy.entity.ScmConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.deploy.entity.QScmConfig.*;

public class ScmConfigCustomRepositoryImpl implements ScmConfigCustomRepository  {

    private final JPAQueryFactory jpaQueryFactory;

    public ScmConfigCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ScmConfig> search(ScmConfigSearchCond condition, Pageable pageable) {

        // contents
        List<ScmConfig> contents = jpaQueryFactory
                .selectFrom(scmConfig)
                .orderBy(scmConfig.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        // total count
        Long totalCount = jpaQueryFactory
                .select(scmConfig.count())
                .from(scmConfig)
                .fetchOne();


        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(contents, pageable, totalCount);
    }


}
