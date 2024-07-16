package com.deploy.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.FileAppender;
import com.deploy.dto.request.JobCreateReq;
import com.deploy.dto.request.JobNotificationUpdateReq;
import com.deploy.dto.request.JobUpdateReq;
import com.deploy.dto.response.JobRes;
import com.deploy.entity.Job;
import com.deploy.entity.Notification;
import com.deploy.entity.Step;
import com.deploy.exception.AppBizException;
import com.deploy.repository.JobRepository;
import com.deploy.repository.NotificationRepository;
import com.deploy.service.event.RunCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.deploy.exception.AppErrorCode.NOT_FOUND_ENTITY_IN_JOB;
import static com.deploy.exception.AppErrorCode.NOT_FOUND_ENTITY_IN_NOTIFICATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final StepService stepService;
    private final RunHistoryService runHistoryService;
    private final JobRepository jobRepository;
    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher eventPublisher;


    /**
     * 내 Job 조회
     * @param id
     * @return
     */
    public List<JobRes> searchMyJob(Long id) {
        List<Job> list = jobRepository.findAllByCreatedByOrderByIdDesc(id);

        List<JobRes> result = list.stream().map(Job -> {
            JobRes jobRes = new JobRes(Job);
            return jobRes;
        }).collect(Collectors.toList());

        return result;
    }

    /**
     * Job 조회
     * @param id
     * @return
     */
    public JobRes findJob(Long id) {
        Job findJob = jobRepository.findById(id)
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_JOB));

        return new JobRes(findJob);
    }


    /**
     * Job 추가
     * @param request
     * @return
     */
    @Transactional
    public Long createJob(JobCreateReq request) {

        String name = request.getName();
        String description = request.getDescription();

        Job job = Job.createJob(name, description);

        // save
        jobRepository.save(job);

        return job.getId();
    }

    /**
     * Job 수정
     * @param id
     * @param request
     * @return
     */
    @Transactional
    public Long updateJob(Long id, JobUpdateReq request) {
        Job findJob = jobRepository.findById(id)
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_JOB));

        // update
        findJob.changeInfo(request.getName(), request.getDescription());

        return id;
    }

    /**
     * Job 삭제
     * @param id
     */
    @Transactional
    public void deleteJob(Long id) {
        Job findJob = jobRepository.findById(id)
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_JOB));

        jobRepository.delete(findJob);
    }


    /**
     * Job 실행 & 메세지 발송
     * @param jobId
     */
    public void runJob(Long jobId) {

        // job 실행
        Long runHistoryId = executeRun(jobId);

        // Message event
        completedRun(runHistoryId);
    }


    public void completedRun(Long runHistoryId) {
        eventPublisher.publishEvent(new RunCompletedEvent(runHistoryId));
    }


    /**
     *
     * @param jobId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long executeRun(Long jobId) {
        // 엔티티 조회
        Job findJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_JOB));

        if (findJob.getSteps().isEmpty()) {
            throw new AppBizException("등록된 Step이 존재하지않습니다. Step 구성을 먼저 수행해주세요.");
        }

        // 동적 로거 생성 & 주입
        Logger historyLogger = runHistoryService.createHistoryLogger(findJob);
        stepService.setHistoryLogger(historyLogger);
        historyLogger.info("====== JobService run start. ======\n");

        // RunHistory 생성
        Long runHistoryId = runHistoryService.createRunHistory(findJob);

        // step 수행
        String prevResult = "";

        for (Step step : findJob.getSteps()) {

            historyLogger.info("==== StepService executeStep start. type={} ====", step.getStepType());

            Long runHistoryDetailId = null;
            Long startTime = System.currentTimeMillis();
            Long endTime = null;

            try {
                // RunHistoryDetails 생성
                runHistoryDetailId = runHistoryService.createRunHistoryDetail(runHistoryId, step);

                // step 실행
                prevResult = stepService.executeStep(step, prevResult);

                // step 성공
                runHistoryService.successDetail(runHistoryDetailId, prevResult);
                historyLogger.info("==== StepService executeStep success. type={} ====\n\n", step.getStepType());

                endTime = System.currentTimeMillis();
                runHistoryService.changeDetailRunTime(runHistoryDetailId, (endTime - startTime));

            } catch (Exception e) {
                // step 실패 처리
                if (runHistoryDetailId != null) {
                    runHistoryService.failDetail(runHistoryDetailId, e.getMessage());
                }
                historyLogger.error("==== StepService executeStep error. message={} ====\n\n", e.getMessage());
            }
        }


        String logFilePath = ((FileAppender<?>) historyLogger.getAppender("FILE-" + jobId)).getFile();
        runHistoryService.completeRun(runHistoryId, logFilePath);
        historyLogger.info("====== JobService run complete. ======");
        return runHistoryId;
    }


    /**
     * Job Notification 설정
     * @param jobId
     * @param request
     * @return
     */
    @Transactional
    public Long updateNotification(Long jobId, JobNotificationUpdateReq request) {
        // 엔티티 조회
        Job findJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_JOB));

        Notification findNotification = notificationRepository.findById(request.getNotificationId())
                .orElseThrow(() -> new AppBizException(NOT_FOUND_ENTITY_IN_NOTIFICATION));


        findJob.setNotification(findNotification);

        return jobId;
    }


}
