package com.deploy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "JOB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Job extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JOB_ID")
    private long id;

    @Column(name = "NAME")
    private String name; // Job 이름

    @Column(name = "DESCRIPTION")
    private String description; // 설명

    @Embedded
    private BuildSet buildSet;

    @OneToMany(mappedBy = "job")
    private List<Step> steps = new ArrayList<>();

    @OneToMany(mappedBy = "job")
    private List<BuildHistory> buildHistories = new ArrayList<>();


    //== 생성 메서드 ==//
    @Builder
    public Job(String name, String description) {
        this.name = name;
        this.description = description;
    }


}
