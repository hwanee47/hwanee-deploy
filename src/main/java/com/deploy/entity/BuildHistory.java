package com.deploy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Getter
@Entity
@Table(name = "BUILD_HISTORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuildHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BUILD_HISTORY_ID")
    private Long id;

    @Column(name = "BUILD_RESULT")
    private boolean buildResult; // 빌드결과

    @Column(name = "BUILD_TIME")
    private Duration buildTime; // 빌드시간

    @Column(name = "BUILD_FAIL_LOG")
    private String buildFailLog; // 빌드실패로그

    @Column(name = "BUILD_FILE_NAME")
    private String buildFileName; // 빌드파일명

    @Column(name = "BUILD_FILE_SIZE")
    private Long buildFileSize; // 빌드파일크기


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JOB_ID")
    private Job job;

}
