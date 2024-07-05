package com.deploy.service;

import com.deploy.dto.request.CredentialCreateReq;
import com.deploy.dto.request.JobCreateReq;
import com.deploy.dto.request.ScmConfigCreateReq;
import com.deploy.dto.request.StepCreateReq;
import com.deploy.dto.response.JobRes;
import com.deploy.entity.Job;
import com.deploy.entity.Step;
import com.deploy.entity.enums.BuildType;
import com.deploy.entity.enums.ScmType;
import com.deploy.entity.enums.StepType;
import com.deploy.exception.AppBizException;
import com.deploy.exception.AppErrorCode;
import com.deploy.repository.JobRepository;
import com.deploy.repository.StepRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class JobServiceTest {

    @Autowired
    JobService jobService;
    @Autowired
    StepService stepService;
    @Autowired
    CredentialService credentialService;
    @Autowired
    ScmConfigService scmConfigService;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    private StepRepository stepRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("JOB 저장 테스트")
    public void test1() {
        //given
        JobCreateReq request = JobCreateReq.builder()
                .name("test")
                .description("description")
                .build();


        //when
        Long id = jobService.createJob(request);
        Job findJob = jobRepository.findById(id)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_JOB));

        //then
        assertThat(findJob.getName()).isEqualTo(request.getName());
        assertThat(findJob.getDescription()).isEqualTo(request.getDescription());

    }


    @Test
    @DisplayName("조회시 존재하지않는 id인 경우 exception 발생")
    public void test2() {
        //given
        JobCreateReq request = JobCreateReq.builder()
                .name("test")
                .description("description")
                .build();

        Long id = jobService.createJob(request);

        //when
        //then
        assertThrows(AppBizException.class, () -> jobService.findJob(33333L));

    }


    @Test
    @DisplayName("BuildNow 시 CI/CD를 수행한다.")
    public void test3() {
        //given
        Long credentialId = createCredential("creadential-1", "hwaneehwanee", "", "", 22, null, null, null);
        Long scmConfigId = createScmConfig(ScmType.GIT, null, "https://github.com/hwanee47/Hwanee-Platform-Server-API.git", "hwanee47", "https://github.com/hwanee47/Hwanee-Platform-Server-API.git", "master");
        Long jobId = createJob("MyJob-1");
        Long stepId_1 = createStep(jobId, StepType.SCM, null, null, scmConfigId, null);
        Long stepId_2 = createStep(jobId, StepType.BUILD, BuildType.GRADLE, null, null, null);
        Long stepId_3 = createStep(jobId, StepType.DEPLOY, null, credentialId, null, null);


        em.flush();
        em.clear();

        //when
        Job findJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_JOB));


        jobService.buildNow(jobId);

//        for (Step step : findJob.getSteps()) {
////            log.info("step :: {}", step.getStepType());
//        }

        //then

    }






    private Long createJob(String name) {
        JobCreateReq request = JobCreateReq.builder()
                .name(name)
                .build();

        return jobService.createJob(request);
    }

    private Long createStep(Long jobId, StepType stepType, BuildType buildType, Long credentialId, Long scmConfigId, String command) {

        StepCreateReq request = StepCreateReq.builder()
                .jobId(jobId)
                .type(stepType)
                .buildType(buildType)
                .credentialId(credentialId)
                .scmConfigId(scmConfigId)
                .command(command)
                .build();

        return stepService.createStep(request);
    }

    private Long createCredential(String name, String username, String password, String host, Integer port, String privateKey, String passphrase, String description) {
        CredentialCreateReq request = CredentialCreateReq.builder()
                .name(name)
                .username(username)
                .password(password)
                .host(host)
                .port(port)
                .privateKey(privateKey)
                .passphrase(passphrase)
                .description(description)
                .build();
        return credentialService.createCredential(request);
    }

    private Long createScmConfig(ScmType type, String description, String url, String username, String password, String branch) {
        ScmConfigCreateReq request = ScmConfigCreateReq.builder()
                .type(type)
                .description(description)
                .url(url)
                .username(username)
                .password(password)
                .branch(branch)
                .build();

        return scmConfigService.createScmConfig(request);
    }

}