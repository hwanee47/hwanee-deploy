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
@Table(name = "BUILD_FILE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuildFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BUILD_FILE_ID")
    private Long id;

    @Comment("빌드파일경로")
    @Column(name = "BUILD_FILE_PATH")
    private String buildFilePath;

    @Comment("비고")
    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "JOB_ID")
    private Job job;

    //== 연관관계 메서드 ==//
    public void setJob(Job job) {
        this.job = job;
        job.getBuildFiles().add(this);
    }

    //== 생성 메서드 ==//
    public static BuildFile createBuildFile(Job job, String buildFilePath) {
        BuildFile buildFile = new BuildFile();
        buildFile.buildFilePath = buildFilePath;
        buildFile.setJob(job);

        return buildFile;
    }


    //== 비즈니스 메서드 ==//
    public void changeInfo(String description) {
        this.description = description;
    }
}
