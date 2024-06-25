package com.deploy.service;

import com.deploy.dto.request.CodeManageConfigCreateReq;
import com.deploy.entity.ScmType;
import com.deploy.repository.CodeManageConfigRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
@Transactional
class CodeManageConfigServiceTest {

    @Autowired
    CodeManageConfigService codeManageConfigService;
    @Autowired
    CodeManageConfigRepository codeManageConfigRepository;
    @Autowired
    EntityManager em;
    @Autowired
    GitService gitService;



    @BeforeEach
    public void createData() {
        CodeManageConfigCreateReq request = CodeManageConfigCreateReq.builder()
                .type(ScmType.GIT)
                .url("https://github.com/hwanee47/Hwanee-Platform-Server-API.git")
                .branch("master")
                .username("hwanee47")
                .password("1234")
                .description("deasd1223")
                .build();

        codeManageConfigService.save(request);
    }


    @Test
    public void 연결성공테스트() throws Exception {
        //given
        ScmType type = ScmType.GIT;
        String url = "https://github.com/hwanee47/Hwanee-Platform-Server-API.git";
        String username = "hwnaee47";
        String password = "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";

        //when
        Boolean isConnedted = codeManageConfigService.isConnected(gitService, url, username, password);

        //then
        assertThat(isConnedted).isTrue();

    }


    @Test
    public void 연결실패테스트() throws Exception {
        //given
        ScmType type = ScmType.GIT;
        String url = "https://github.com/hwanee47/Hwanee-Platform-Server-API.git";
        String username = "hwnaee47";
        String password = "1234";


        //when & then
        assertThrows(GitAPIException.class, () -> codeManageConfigService.isConnected(gitService, url, username, password));

    }






}