package com.deploy.service;

import com.deploy.dto.request.JobCreateReq;
import com.deploy.dto.response.JobRes;
import com.deploy.entity.Job;
import com.deploy.exception.AppBizException;
import com.deploy.exception.AppErrorCode;
import com.deploy.repository.JobRepository;
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
    JobRepository jobRepository;

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
    public void test() {
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

}