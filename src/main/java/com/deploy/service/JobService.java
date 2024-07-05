package com.deploy.service;

import com.deploy.dto.request.JobCreateReq;
import com.deploy.dto.request.JobUpdateReq;
import com.deploy.dto.response.JobRes;
import com.deploy.entity.Job;
import com.deploy.entity.ScmConfig;
import com.deploy.entity.Step;
import com.deploy.entity.enums.ScmType;
import com.deploy.entity.enums.StepType;
import com.deploy.exception.AppBizException;
import com.deploy.exception.AppErrorCode;
import com.deploy.repository.JobRepository;
import com.deploy.service.utils.GitService;
import com.deploy.service.utils.ScmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.deploy.entity.enums.StepType.*;
import static com.deploy.entity.enums.StepType.BUILD;
import static com.deploy.entity.enums.StepType.SCM;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobService {

    private final StepService stepService;
    private final JobRepository jobRepository;

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


    // Build Now
    @Transactional
    public void buildNow(Long id) {

        // 엔티티 조회
        Job findJob = jobRepository.findById(id)
                .orElseThrow(() -> new AppBizException(AppErrorCode.NOT_FOUND_ENTITY_IN_JOB));

        // step 조회 & 수행
        for (Step step : findJob.getSteps()) {
            stepService.executeStep(step);
        }

    }


}
