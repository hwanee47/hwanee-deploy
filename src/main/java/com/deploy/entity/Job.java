package com.deploy.entity;

import com.deploy.entity.embed.BuildSet;
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
    private Long id;

    @Column(name = "NAME")
    private String name; // Job 이름

    @Column(name = "DESCRIPTION")
    private String description; // 설명

    @Embedded
    private BuildSet buildSet;

    @ManyToOne
    @JoinColumn(name = "CODE_MANAGE_CONFIG_ID")
    private ScmConfig scmConfig;

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
