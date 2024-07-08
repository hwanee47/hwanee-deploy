package com.deploy.entity;

import com.deploy.entity.enums.ScmType;
import com.deploy.entity.enums.StepType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
@Table(name = "RUN_HISTORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RUN_HISTORY_ID")
    private Long id;

    @Comment("JOB 실행 차수")
    @Column(name = "JOB_RUN_SEQ")
    private Long jobRunSeq;

    @Comment("실행 결과")
    @Column(name = "RUN_RESULT")
    private boolean runResult;

    @Comment("총 실행 시간")
    @Column(name = "TOTAL_RUN_TIME")
    private Long totalRunTime;

    @Comment("로그 파일 경로")
    @Column(name = "LOG_FILE_PATH")
    private String logFilePath;

    @OneToMany(mappedBy = "runHistory", cascade = CascadeType.ALL)
    private List<RunHistoryDetail> runHistoryDetails = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "JOB_ID")
    private Job job;


    //== 연관관계 메서드 ==//
    public void setJob(Job job) {
        this.job = job;
        job.getRunHistories().add(this);
    }

    public void setRunHistoryDetails(RunHistoryDetail runHistoryDetail) {
        runHistoryDetail.setRunHistory(this);
        this.runHistoryDetails.add(runHistoryDetail);
    }


    //== 생성 메서드 ==//
    public static RunHistory createRunHistory(Job job, Long jobRunSeq, String logFilePath, RunHistoryDetail... runHistoryDetails) {
        RunHistory runHistory = new RunHistory();
        runHistory.jobRunSeq = (jobRunSeq == null) ? 1L : jobRunSeq + 1;
        runHistory.logFilePath = logFilePath;
        runHistory.setJob(job);

        return runHistory;
    }


    //== 비즈니스 메서드 ==//
    // 실행 완료
    public void completeRun() {

        boolean result = true;
        long totalRunTime = 0L;

        for (RunHistoryDetail runHistoryDetail : runHistoryDetails) {
            totalRunTime += runHistoryDetail.getRunTime();

            if (!runHistoryDetail.isSuccess()) {
                result = false;
            }
        }

        this.runResult = result;
        this.totalRunTime = totalRunTime;
    }

    // StepType으로 Details 리스트의 인덱스 구하기
    public int getDetailsIndexByStepType(StepType stepType) {
        int index = 0;
        for (RunHistoryDetail runHistoryDetail : runHistoryDetails) {
            if (runHistoryDetail.getStep().getStepType().equals(stepType)) {
                return index;
            }
            index++;
        }

        throw new IllegalStateException("해당 유형(StepType)에 맞는 RunHistoryDetails가 존재하지 않습니다. stepType="+stepType);
    }


}
