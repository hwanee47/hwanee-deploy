package com.deploy.service;

import com.deploy.dto.request.JobCreateReq;
import com.deploy.dto.request.JobUpdateReq;
import com.deploy.dto.response.JobRes;
import com.deploy.entity.Job;
import com.deploy.entity.RunHistory;
import com.deploy.entity.RunHistoryDetail;
import com.deploy.entity.Step;
import com.deploy.exception.AppBizException;
import com.deploy.exception.AppErrorCode;
import com.deploy.repository.JobRepository;
import com.deploy.repository.RunHistoryDetailRepository;
import com.deploy.repository.RunHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    private final StepService stepService;
    private final JobRepository jobRepository;
    private final RunHistoryRepository runHistoryRepository;
    private final RunHistoryDetailRepository runHistoryDetailRepository;

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
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_JOB));

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
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_JOB));

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
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_JOB));

        jobRepository.delete(findJob);
    }


    /**
     * Job 작업수행
     * @param jobId
     */
    @Transactional
    public void runJob(Long jobId) {

        // 엔티티 조회
        Job findJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_JOB));

        // RunHistory 생성
        Long maxSeq = runHistoryRepository.findMaxSeq(jobId);
        RunHistory runHistory = RunHistory.createRunHistory(findJob, maxSeq, "");

        // step 수행
        String prevResult = "";
        for (Step step : findJob.getSteps()) {

            // RunHistoryDetails 생성 & RunHistory에 추가
            RunHistoryDetail runHistoryDetail = RunHistoryDetail.createRunHistoryDetail(step);
            runHistory.setRunHistoryDetails(runHistoryDetail);

            Long startTime = System.currentTimeMillis();
            Long endTime = null;

            try {
                // step 실행
                prevResult = stepService.executeStep(step, prevResult);

                // step 성공
                runHistoryDetail.success(prevResult);

            } catch (Exception e) {
                // step 실패
                runHistoryDetail.fail(e.getMessage());
                log.error("StepService executeStep error. message={}", e.getMessage());
            } finally {
                endTime = System.currentTimeMillis();
                runHistoryDetail.changeRunTime(endTime - startTime);
            }

        }

        runHistory.completeRun();

        // RunHistory 저장 (cascade)
        runHistoryRepository.save(runHistory);

    }


}
