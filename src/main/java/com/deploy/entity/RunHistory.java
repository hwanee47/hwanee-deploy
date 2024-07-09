package com.deploy.entity;

import com.deploy.entity.enums.HistoryStatus;
import com.deploy.entity.enums.StepType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

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

    @Comment("상태: ING(진행중), COMPLETE(완료)")
    @Enumerated(EnumType.STRING)
    private HistoryStatus status;

    @Comment("성공 여부")
    @Column(name = "IS_SUCCESS")
    private boolean isSuccess;

    @Comment("총 실행 시간")
    @Column(name = "TOTAL_RUN_TIME")
    private Long totalRunTime;

    @Comment("로그 파일 경로")
    @Column(name = "LOG_FILE_PATH")
    private String logFilePath;

    @OneToMany(mappedBy = "runHistory")
    private List<RunHistoryDetail> runHistoryDetails = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "JOB_ID")
    private Job job;


    //== 연관관계 메서드 ==//
    public void setJob(Job job) {
        this.job = job;
        job.getRunHistories().add(this);
    }


    //== 생성 메서드 ==//
    public static RunHistory createRunHistory(Job job, Long jobRunSeq, String logFilePath, RunHistoryDetail... runHistoryDetails) {
        RunHistory runHistory = new RunHistory();
        runHistory.jobRunSeq = (jobRunSeq == null) ? 1L : jobRunSeq + 1;
        runHistory.logFilePath = logFilePath;
        runHistory.status = HistoryStatus.ING; // 초기상태 : 진행중
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

        this.isSuccess = result;
        this.totalRunTime = totalRunTime;
        this.status = HistoryStatus.COMPLETE;
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
