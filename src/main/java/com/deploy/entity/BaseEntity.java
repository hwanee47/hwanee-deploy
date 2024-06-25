package com.deploy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class BaseEntity {

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;    // 생성일시

    @Column(name = "CREATED_BY", updatable = false)
    private long createdBy;             // 생성자

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;    // 수정일시

    @Column(name = "UPDATED_BY")
    private long updatedBy;             // 수정자

}
