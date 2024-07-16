package com.deploy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "JOB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Job extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JOB_ID")
    private Long id;

    @Comment("Job 이름")
    @Column(name = "NAME")
    private String name;

    @Comment("설명")
    @Column(name = "DESCRIPTION")
    private String description;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "NOTIFICATION_ID")
    private Notification notification;

    @OneToMany(mappedBy = "job")
    private List<Step> steps = new ArrayList<>();

    @OneToMany(mappedBy = "job")
    private List<RunHistory> runHistories = new ArrayList<>();

    @OneToMany(mappedBy = "job")
    private List<BuildFile> buildFiles = new ArrayList<>();


    //== 연관관계 메서드 ==//
    public void setNotification(Notification notification) {
        this.notification = notification;
        notification.setJob(this);
    }

    //== 생성 메서드 ==//
    public static Job createJob(String name, String description) {
        Job job = new Job();
        job.name = name;
        job.description = description;
        return job;
    }

    @Builder
    public Job(String name, String description) {
        this.name = name;
        this.description = description;
    }


    //== 비즈니스 메서드 ==//
    public void changeInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
