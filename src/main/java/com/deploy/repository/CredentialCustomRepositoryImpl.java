package com.deploy.repository;

import com.deploy.dto.request.CredentialSearchCond;
import com.deploy.entity.Credential;
import com.deploy.entity.Job;
import com.deploy.entity.QCredential;
import com.deploy.entity.QJob;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.deploy.entity.QCredential.credential;

public class CredentialCustomRepositoryImpl implements CredentialCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CredentialCustomRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Credential> search(CredentialSearchCond condition, Pageable pageable) {

        // contents
        List<Credential> contents = jpaQueryFactory
                .selectFrom(credential)
                .orderBy(credential.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        // total count
        Long totalCount = jpaQueryFactory
                .select(credential.count())
                .from(credential)
                .fetchOne();


        if (totalCount == null) {
            totalCount = 0L;
        }

        return new PageImpl<>(contents, pageable, totalCount);
    }
}
