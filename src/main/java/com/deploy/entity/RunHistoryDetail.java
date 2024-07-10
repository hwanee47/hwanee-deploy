package com.deploy.entity;

import com.deploy.entity.enums.HistoryStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.Duration;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "RUN_HISTORY_DETAIL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RunHistoryDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RUN_HISTORY_DETAIL_ID")
    private Long id;

    @Comment("상태: ING(진행중), COMPLETE(완료)")
    @Enumerated(EnumType.STRING)
    private HistoryStatus status;

    @Comment("STEP별 성공 여부")
    @Column(name = "IS_SUCCESS")
    private boolean isSuccess;

    @Comment("STEP별 실행 결과")
    @Column(name = "RUN_RESULT")
    private String runResult;

    @Comment("STEP별 실행 시간")
    @Column(name = "RUN_TIME")
    private Long runTime;

    @Comment("실패 로그")
    @Column(name = "RUN_FAIL_LOG")
    private String runFailLog;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "STEP_ID")
    private Step step;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "RUN_HISTORY_ID")
    private RunHistory runHistory;

    //== 연관관계 메서드 == //
    public void setRunHistory(RunHistory runHistory) {
        this.runHistory = runHistory;
        runHistory.getRunHistoryDetails().add(this);
    }


    //== 생성 메서드 ==//
    public static RunHistoryDetail createRunHistoryDetail(Step step) {
        RunHistoryDetail runHistoryDetail = new RunHistoryDetail();
        runHistoryDetail.step = step;
        runHistoryDetail.status = HistoryStatus.ING; // 초기상태: 진행중

        return runHistoryDetail;
    }


    //== 비즈니스 메서드 ==//
    public void changeRunTime(Long runTime) {
        this.runTime = runTime;
    }

    public void success(String result) {
        this.isSuccess = true;
        this.runResult = result;
        this.runFailLog = null;
        this.status = HistoryStatus.COMPLETE;
    }

    public void fail(String message) {
        this.isSuccess = false;
        this.runFailLog = message;
        this.status = HistoryStatus.COMPLETE;
    }

}
