package com.deploy.entity.embed;

import com.deploy.entity.enums.BuildType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuildSet {

    @Comment("빌드유형 (MAVEN, GRADLE ..)")
    @Enumerated(EnumType.STRING)
    @Column(name = "BUILD_TYPE")
    private BuildType buildType;

    @Comment("빌드테스트 유무")
    @Column(name = "IS_BUILD_TEST")
    private boolean isBuildTest;

    @Comment("빌드파일경로")
    @Column(name = "BUILD_FILE_PATH")
    private String buildFilePath;

    public BuildSet(BuildType buildType, String buildFilePath, boolean isBuildTest) {
        this.buildType = buildType;
        this.buildFilePath = buildFilePath;
        this.isBuildTest = isBuildTest;
    }
}
