package com.deploy.service;

import com.deploy.dto.request.JobCreateReq;
import com.deploy.dto.request.ScheduleCreateReq;
import com.deploy.dto.request.ScheduleUpdateReq;
import com.deploy.entity.Job;
import com.deploy.entity.Schedule;
import com.deploy.exception.AppBizException;
import com.deploy.exception.AppErrorCode;
import com.deploy.repository.JobRepository;
import com.deploy.repository.ScheduleRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.deploy.exception.AppErrorCode.NOT_FOUND_ENTITY_IN_SCHEDULE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
@Transactional
class ScheduleServiceTest {

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    EntityManager em;


    @Test
    @DisplayName("스케줄 등록 테스트")
    public void test1() {

        //given
        Long jobId = createJob();
        ScheduleCreateReq request = ScheduleCreateReq.builder()
                .jobId(jobId)
                .time(LocalDateTime.now())
                .description("test")
                .build();

        //when
        Long scheduleId = scheduleService.createSchedule(request);
        Schedule findSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_SCHEDULE));

        //then
        assertThat(findSchedule.getScheduleTime()).isEqualTo(request.getTime());
        assertThat(findSchedule.getJob().getId()).isEqualTo(jobId);


    }


    @Test
    @DisplayName("스케줄 수정 테스트")
    public void test2() {
        //given
        ScheduleCreateReq request = ScheduleCreateReq.builder()
                .time(LocalDateTime.now())
                .description("test")
                .build();

        Long scheduleId = scheduleService.createSchedule(request);


        ScheduleUpdateReq updateRequest = ScheduleUpdateReq.builder()
                .time(LocalDateTime.now())
                .description("test222")
                .build();

        //when
        scheduleService.updateSchedule(scheduleId, updateRequest);
        Schedule findSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_SCHEDULE));


        //then
        assertThat(findSchedule.getScheduleTime()).isEqualTo(updateRequest.getTime());
        assertThat(findSchedule.getDescription()).isEqualTo(updateRequest.getDescription());

    }


    @Test
    @DisplayName("이미 실행된 스케줄은 수정할 수 없다.")
    public void test3() {
        //given
        ScheduleCreateReq request = ScheduleCreateReq.builder()
                .time(LocalDateTime.now())
                .description("test")
                .build();

        Long scheduleId = scheduleService.createSchedule(request);
        Schedule findSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_SCHEDULE));

        // 실행처리
        findSchedule.executedSchedule();

        em.flush();
        em.clear();

        log.info("강제 flush");

        ScheduleUpdateReq updateRequest = ScheduleUpdateReq.builder()
                .time(LocalDateTime.now())
                .description("test222")
                .build();

        //when & then
        assertThrows(AppBizException.class, () -> scheduleService.updateSchedule(scheduleId, updateRequest));

    }


    private Long createJob() {
        Job job = Job.createJob("job1", "");
        jobRepository.save(job);
        return job.getId();
    }
}