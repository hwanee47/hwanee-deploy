package com.deploy.service;

import com.deploy.entity.ScmType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class GitServiceTest {

    @Autowired
    GitService gitService;

    @Test
    @DisplayName("깃헙 프로젝트 클론 테스트")
    void cloneProjectTest() throws GitAPIException, IOException {

        String url = "https://github.com/hwanee47/Hwanee-Platform-Server-API.git";
        String branch = "master";
        String username = "hwanee47";
        String password = "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";
        String clonePath = "/Users/hwaneehwanee/test/project";

        gitService.cloneProject(url, branch, username, password, clonePath);
    }


    @Test
    @DisplayName("자격증명이 실패하는경우 에러")
    void cloneProjectTest2() throws Exception {
        String url = "https://github.com/hwanee47/Hwanee-Platform-Server-API.git";
        String branch = "master";
        String username = "hwanee47";
        String password = "1234";
        String clonePath = "/Users/hwaneehwanee/test/project";

        assertThrows(GitAPIException.class, () -> gitService.cloneProject(url, branch, username, password, clonePath));
    }

    @Test
    @DisplayName("브랜치가 없는경우 에러")
    void cloneProjectTest3() throws Exception {
        String url = "https://github.com/hwanee47/Hwanee-Platform-Server-API.git";
        String branch = "notfoundbranch";
        String username = "hwanee47";
        String password = "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";
        String clonePath = "/Users/hwaneehwanee/test/project";

        assertThrows(GitAPIException.class, () -> gitService.cloneProject(url, branch, username, password, clonePath));
    }


}