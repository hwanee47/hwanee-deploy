package com.deploy.entity.embed;

import com.deploy.entity.enums.BuildType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
@Embeddable
public class BuildSet {

    @Enumerated(EnumType.STRING)
    @Column(name = "BUILD_TYPE")
    private BuildType buildType; // 빌드유형 (MAVEN, GRADLE ..)

    @Column(name = "BUILD_FILE_PATH")
    private String buildFilePath; // 빌드파일경로

}
