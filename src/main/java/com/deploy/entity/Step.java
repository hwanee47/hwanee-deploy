package com.deploy.entity;

import com.deploy.entity.enums.StepType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CREDENTIAL_ID")
    private Credential credential; // 자격증명

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "SCM_CONFIG_ID")
    private ScmConfig scmConfig; // 코드관리

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "JOB_ID")
    private Job job;


    //== 연관관계 메서드 ==//
    public void setJob(Job job) {
        this.job = job;
        job.getSteps().add(this);
    }


    //== 생성 메서드 ==//
    public static Step createStep(Long stepIndex, StepType stepType, String command, Job job, Credential credential, ScmConfig scmConfig) {

        Step step = new Step();
        step.stepIndex = stepIndex;
        step.stepType = stepType;
        step.command = command;
        step.credential = credential;
        step.scmConfig = scmConfig;

        step.setJob(job);

        return step;
    }


    //== 비즈니스 로직 ==//

}
