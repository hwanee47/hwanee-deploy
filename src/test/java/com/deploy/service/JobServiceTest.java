package com.deploy.service;

import com.deploy.dto.request.CredentialCreateReq;
import com.deploy.dto.request.JobCreateReq;
import com.deploy.dto.request.ScmConfigCreateReq;
import com.deploy.dto.request.StepCreateReq;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        Long credentialId = createCredential("creadential-1", "ec2-user", "", "35.172.184.110", 22, null, null, null);
        Long scmConfigId = createScmConfig(ScmType.GIT, null, "https://github.com/hwanee47/hwanee-deploy.git", "hwanee47", "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", "main");
        Long jobId = createJob("MyJob-1");
        Long stepId_1 = createStep(jobId, StepType.SCM, null, null, scmConfigId, null);
        Long stepId_2 = createStep(jobId, StepType.BUILD, BuildType.GRADLE, null, null, null);
        Long stepId_3 = createStep(jobId, StepType.DEPLOY, null, credentialId, null, null);


        em.flush();
        em.clear();

        //when
        Job findJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_JOB));


        jobService.runJob(jobId);


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
                .privateKey(
                        "-----BEGIN RSA PRIVATE KEY-----\n" +
                        "MIIEpAIBAAKCAQEA61lhUBHpDwJ2FGzglmNpqHvOXdCi6DuHhT+rIvoe93P9c5MS\n" +
                        "YAwCKcGYcQ3d9lFXDlL1B4YLSZlUJHW73NTEHRIVBRWi+EXwLehN9W3fR/mC/7X9\n" +
                        "xiRCXn1qXTKpndVCUEKbQKHk1PDivqQxcyPRfB1si8cYIPCZkGGlo+GazCOmt6Gm\n" +
                        "9VZpVgCAlTiKOiFF7Qdm6LrRCaI4TtbZIyM09HExCDnxp7OagK2RWPsPM+fnG3i0\n" +
                        "GH25wxreh+0bAKwM7LEqi+hVTKFaFn9rLVgJWpy43ASpnPH8Tl+X44PTsbPYRaKn\n" +
                        "RYVCQtN0FAqzTBnZqRqwW3QtjUtAfry5qLhwZwIDAQABAoIBAQCVxvAzcPKNZteW\n" +
                        "8COOEf5wBzqyYdELUrdQidB0FhIXEW4/W13aWkoWIOrPKDAWTnXE2+6stxX/5OCz\n" +
                        "w2mnhJC6n4NZFQf+USQlUy4p/56Vw6km679xlinW0KIcZd2kYNAvG/SKEX38NsFW\n" +
                        "6k++IxFyl5c47Z/hdr2EMWClBzLorp9B4mmsH7rZ5+UTU9hU/nLsZ/F7wTknIQj7\n" +
                        "8NvWEnW8RLlIW5jRiovSEH9EaGMK/B5Bw3qm+DWlxXNCJ4o9YwZoh0iNCJmlwnMf\n" +
                        "Sf2GWaxtmnHJqastalV54nCxHf7b+Yzo8jYaNee9MvQ6jlqQdmie7/PKuohf2P/p\n" +
                        "6GecREHhAoGBAPZpJT8wr+k1kIDGpyMo3qzkKQyNlzZW0sh8ZP1nCBo+Ch/QDBxM\n" +
                        "T3E6+nbijM9WLHpd9K940N7YWUUq2J3/q1ugTWeBO+iEWUcjzKIth4hVEODjOBTq\n" +
                        "SBKdNjRroAfV03dn1wmqcsRhgL6o59p5eO6fg+RwG14PgmhWuJsmYYoFAoGBAPSC\n" +
                        "CL7JxeiI6ZXSfZdt08v2N12ZQz7sGbRwGqb62IV+0790sYVvNVU3SS3tMGhB6HHi\n" +
                        "dIxH9Yx8pczgGFDhAQ/mDE1jZWe10W9f0tg4HcaIJLS3DZztpuyjC4SXWTobtU8y\n" +
                        "zG6DWvTKDl9nokmlFE3fxiobhwv41DaWsWU2uKB7AoGBANgf/SVhhMldy/LHSo2S\n" +
                        "KU0nicGS5xAoMxTZ1pJULk0mIScqCZVAcWV1P33K04p/oN9rTVQi+cCbriD2paxf\n" +
                        "NWNWRM4GEg+tFclJ4xBUMs4nHnjBksz1eGWrMoHj7CczKxlOINQ/hg4tHwkbiNCd\n" +
                        "Pq69hqd0lOx5Wf4+IgkjLuYpAoGATjaaXY1lxXCmZ8qhaiMzsPbd1w8Dt8kGn/WM\n" +
                        "UQXE5U8gpQnLD4f3Y37/5bUN2wvaMzPhXE5YecwVrWex341aLZ/FJ6w37+j1Sc85\n" +
                        "PvkUbUF3nGdB74UF9IRjVtKjNDdQ1DjHtEJIgi1wU4xvGWe5CwAd/7I2jNnX6G5j\n" +
                        "6KCMhqkCgYAVpuFxxeb75i1GnKZWbaR2rgcLUVWz/XevCHMJg3d47SSpQTdFwlYL\n" +
                        "K2b9LbukPpxG/qWvbfwjuxI3EHGViezlZ/uUlQLX1mQVUb+B9ZTyEy6UgxgjMPzw\n" +
                        "8HzWOXClz68jg8tRxvxOqNmI+n4LLQFlE6BgbUPvrT1f1AZ9vJp5+g==\n" +
                        "-----END RSA PRIVATE KEY-----"
                )
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