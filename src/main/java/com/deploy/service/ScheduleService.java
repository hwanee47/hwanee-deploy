package com.deploy.service;

import com.deploy.dto.request.ScheduleCreateReq;
import com.deploy.dto.request.ScheduleUpdateReq;
import com.deploy.dto.response.ScheduleRes;
import com.deploy.entity.Job;
import com.deploy.entity.Schedule;
import com.deploy.exception.AppBizException;
import com.deploy.repository.JobRepository;
import com.deploy.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.deploy.exception.AppErrorCode.NOT_FOUND_ENTITY_IN_JOB;
import static com.deploy.exception.AppErrorCode.NOT_FOUND_ENTITY_IN_SCHEDULE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final JobRepository jobRepository;
    private final JobService jobService;
    private final PlatformTransactionManager transactionManager;


    /**
     * 예약 리스트 조회
     * @param jobId
     * @param pageable
     * @return
     */
    public Page<ScheduleRes> searchByJobId(Long jobId, Pageable pageable) {
        Page<Schedule> list = scheduleRepository.searchByJobId(jobId, pageable);

        List<ScheduleRes> results = list.stream()
                .map(Schedule -> ScheduleRes.builder()
                        .id(Schedule.getId())
                        .createdAt(Schedule.getCreatedAt())
                        .scheduleTime(Schedule.getScheduleTime())
                        .executedTime(Schedule.getExecutedTime())
                        .description(Schedule.getDescription())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, list.getTotalElements());
    }


    /**
     * Schedule 조회
     * @param scheduleId
     * @return
     */
    public ScheduleRes findSchedule(Long scheduleId) {
        // 엔티티 조회
        Schedule findSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_SCHEDULE));

        return ScheduleRes.builder()
                .id(findSchedule.getId())
                .createdAt(findSchedule.getCreatedAt())
                .scheduleTime(findSchedule.getScheduleTime())
                .executedTime(findSchedule.getExecutedTime())
                .description(findSchedule.getDescription())
                .build();
    }


    /**
     * 스케줄 저장
     * @param request
     */
    @Transactional
    public Long createSchedule(ScheduleCreateReq request) {

        Long jobId = request.getJobId();
        LocalDateTime time = request.getTime();
        String description = request.getDescription();

        // 엔티티 조회
        Job findJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_JOB));

        Schedule schedule = Schedule.createSchedule(findJob, time, description);
        scheduleRepository.save(schedule);

        return schedule.getId();
    }


    /**
     * 스케줄 수정
     * @param scheduleId
     * @param request
     * @return
     */
    @Transactional
    public Long updateSchedule(Long scheduleId, ScheduleUpdateReq request) {

        LocalDateTime time = request.getTime();
        String description = request.getDescription();

        // 엔티티 조회
        Schedule findSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_SCHEDULE));

        // update
        findSchedule.changeInfo(time, description);

        return scheduleId;
    }


    /**
     * 스케줄 삭제
     * @param scheduleId
     */
    @Transactional
    public void deleteSchedule(Long scheduleId) {

        // 엔티티 조회
        Schedule findSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_SCHEDULE));


        if (findSchedule.isExecuted())
            throw new AppBizException("실행완료된 스케줄은 삭제불가능합니다.");

        scheduleRepository.delete(findSchedule);

    }


    /**
     * 스케줄 실행
     * - 1분 마다 실행
     */
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void runScheduledTasks() throws Exception {

        /**
         * 트랜잭션을 수동으로 설정한 이유
         *  - 스케줄 상태 업데이트 해야하기 때문에 현 메서드에서 트랜잭션 관리가 필요했음.
         *  - 현 메서드 "전체"범위로 트랜잭션을 잡으면 jobService.runJob이 트랜잭션 범위에 참여하기 때문에 정상동작하지 않음.
         *  - 스케줄 상태 업데이트와 작업실행 각각 따로 트랜잭션 관리가 필요했음.
         */

        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status1 = transactionManager.getTransaction(definition);

        try {
            LocalDateTime now = LocalDateTime.now();
            List<Schedule> schedules = scheduleRepository.findByExecutedFalseAndScheduleTimeLessThanEqual(now);

            for (Schedule schedule : schedules) {

                // 스케줄 상태 업데이트
                schedule.executedSchedule();
                transactionManager.commit(status1);


                // 작업실행
                Long runHistoryId = jobService.executeRun(schedule.getJob().getId());

                // 메세지 발송
                jobService.completedRun(runHistoryId);

            }
        } catch (Exception e) {
            transactionManager.rollback(status1);
            e.printStackTrace();
            throw e;
        }
    }
}
