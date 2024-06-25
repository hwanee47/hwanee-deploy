package com.deploy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "STEP")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Step extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STEP_ID")
    private Long id;

    @Column(name = "STEP_INDEX")
    private Long stepIndex; // 순서

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JOB_ID")
    private Job job;


    //== 연관관계 메서드 ==//
    void setJob(Job job) {
        this.job = job;
        job.getSteps().add(this);
    }


    //== 생성 메서드 ==//


    //== 비즈니스 로직 ==//


}
