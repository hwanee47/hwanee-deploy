package com.deploy.entity;

import com.deploy.exception.AppBizException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static java.time.LocalDateTime.now;

@Getter
@Entity
@Table(name = "SCHEDULE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_ID")
    private Long id;

    @Comment("스케줄 일시")
    @Column(name = "SCHEDULE_TIME")
    private LocalDateTime scheduleTime;

    @Comment("비고")
    @Column(name = "DESCRIPTION")
    private String description;

    @Comment("실행여부")
    @Column(name = "EXECUTED")
    private boolean executed;

    @Comment("실행일시")
    @Column(name = "EXECUTED_TIME")
    private LocalDateTime executedTime;

    @ManyToOne(fetch = LAZY)
    private Job job;

    //== 생성 메서드 ==//
    public static Schedule createSchedule(Job job, LocalDateTime scheduleTime, String description) {

        if (scheduleTime.compareTo(LocalDateTime.now()) < 0)
            throw new AppBizException("미래 시간만 설정 가능합니다.");


        Schedule schedule = new Schedule();
        schedule.job = job;
        schedule.scheduleTime = scheduleTime;
        schedule.description = description;
        schedule.executed = false;

        return schedule;
    }


    //== 비즈니스 메서드==//
    public void executedSchedule() {
        this.executed = true;
        this.executedTime = now();
    }


    public void changeInfo(LocalDateTime scheduleTime, String description) {

        if (this.executed)
            throw new AppBizException("이미 실행된 스케줄은 수정할 수 없습니다.");

        if (scheduleTime.compareTo(LocalDateTime.now()) < 0)
            throw new AppBizException("미래 시간만 설정 가능합니다.");

        this.scheduleTime = scheduleTime;
        this.description = description;

    }

}
