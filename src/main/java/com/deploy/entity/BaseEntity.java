package com.deploy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Comment("생성일시")
    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;    // 생성일시

    @Comment("생성자")
    @CreatedBy
    @Column(name = "CREATED_BY", updatable = false)
    private Long createdBy;             // 생성자

    @Comment("수정일시")
    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;    // 수정일시

    @Comment("수정자")
    @LastModifiedBy
    @Column(name = "UPDATED_BY")
    private Long updatedBy;             // 수정자

}
