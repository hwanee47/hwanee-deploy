package com.deploy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "BUILD_CONFIG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuildConfig extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BUILD_CONFIG_ID")
    private long id;

    @Embedded
    private BuildSet buildSet;

}
