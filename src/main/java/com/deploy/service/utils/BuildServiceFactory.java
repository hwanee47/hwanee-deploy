package com.deploy.service.utils;

import com.deploy.entity.enums.BuildType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static com.deploy.entity.enums.BuildType.*;

@Component
@RequiredArgsConstructor
public class BuildServiceFactory {

    private final GradleService gradleService;
    private final MavenService mavenService;

    public void setHistoryLogger(Logger logger) {
        gradleService.setHistoryLogger(logger);
        mavenService.setHistoryLogger(logger);
    }

    public BuildService getBuildService(BuildType type) {
        switch (type) {
            case GRADLE:
                return gradleService;
            case MAVEN:
                return mavenService;
            default:
                throw new IllegalArgumentException("알수없는 Build 유형 입니다. type: " + type);
        }
    }
}
