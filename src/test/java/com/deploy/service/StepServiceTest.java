package com.deploy.service;

import com.deploy.dto.request.StepCreateReq;
import com.deploy.entity.*;
import com.deploy.entity.enums.ScmType;
import com.deploy.entity.enums.StepType;
import com.deploy.repository.CredentialRepository;
import com.deploy.repository.JobRepository;
import com.deploy.repository.ScmConfigRepository;
import com.deploy.repository.StepRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@Rollback(false)
class StepServiceTest {

    @Autowired
    StepService stepService;

    @Autowired
    StepRepository stepRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    CredentialRepository credentialRepository;

    @Autowired
    ScmConfigRepository scmConfigRepository;

    @BeforeEach
    public void init() {
        createJob("job1", "test1");

        createCredential("git-credential", "hwaneehwanee", "192.0.0.1", 22, "123123123");

        createScmConfig(ScmType.GIT, "http://github.com", "hwaneehwanee", "token", "master");
    }


    @Test
    @DisplayName("Step 엔티티 저장 테스트")
    public void test1() {
        //given
        List<Job> jobs = jobRepository.findAll();
        List<Credential> credentials = credentialRepository.findAll();
        List<ScmConfig> scmConfigs = scmConfigRepository.findAll();

        StepCreateReq stepCreateReq = StepCreateReq.builder()
                .type(StepType.SCM)
                .command("test")
                .jobId(jobs.get(0).getId())
                .credentialId(credentials.get(0).getId())
                .scmConfigId(scmConfigs.get(0).getId())
                .build();

        //when
        Long savedId = stepService.createStep(stepCreateReq);
        Step findStep = stepRepository.findById(savedId)
                .orElseThrow(() -> new IllegalArgumentException("No such data."));

        //then
        assertThat(stepCreateReq.getType()).isEqualTo(findStep.getStepType());

    }




    private void createScmConfig(ScmType scmType, String url, String username, String password, String branch) {
        ScmConfig scmConfig = ScmConfig.builder()
                .scmType(scmType)
                .url(url)
                .username(username)
                .password(password)
                .branch(branch)
                .build();
        scmConfigRepository.save(scmConfig);
    }

    private void createCredential(String identifierName, String targetUsername, String targetHost, int targetPort, String privateKey) {
        Credential credential = Credential.builder()
                .identifierName(identifierName)
                .targetUsername(targetUsername)
                .targetHost(targetHost)
                .targetPort(targetPort)
                .privateKey(privateKey)
                .build();
        credentialRepository.save(credential);
    }

    private void createJob(String name, String description) {
        Job job = Job.builder()
                .name(name)
                .description(description)
                .build();
        jobRepository.save(job);
    }

}