package com.deploy.entity;

import com.deploy.entity.enums.StepType;
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

    @Column(name = "STEP_TYPE")
    private StepType stepType; // 유형

    @Lob
    @Column(name = "COMMAND")
    private String command; // 명령어

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREDENTIAL_ID")
    private Credential credential; // 자격증명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCM_CONFIG_ID")
    private ScmConfig scmConfig; // 코드관리

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
