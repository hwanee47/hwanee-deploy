package com.deploy.service;

import com.deploy.service.utils.GradleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class GradleServiceTest {

    @Autowired
    GradleService gradleService;

    @Test
    public void buildTest() {

        String projectPath = "/Users/hwaneehwanee/test/hwanee-deploy";

        try {
            gradleService.executeBuild(projectPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}