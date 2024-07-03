package com.deploy.service;

import com.deploy.dto.request.JobCreateReq;
import com.deploy.dto.request.JobUpdateReq;
import com.deploy.dto.response.JobRes;
import com.deploy.dto.response.ScmConfigRes;
import com.deploy.entity.Credential;
import com.deploy.entity.Job;
import com.deploy.exception.AppBizException;
import com.deploy.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobService {

    private final StepService service;
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
                .orElseThrow(() -> new AppBizException("No such data in Job."));

        return new JobRes(findJob.getName(), findJob.getDescription());
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
                .orElseThrow(() -> new AppBizException("No such data in Job."));

        // update
        findJob.changeInfo(findJob.getName(), findJob.getDescription());

        return id;
    }

    /**
     * Job 삭제
     * @param id
     */
    @Transactional
    public void deleteJob(Long id) {
        Job findJob = jobRepository.findById(id)
                .orElseThrow(() -> new AppBizException("No such data in Job."));

        jobRepository.delete(findJob);
    }


    // CI/CD 시작
    @Transactional
    public void run(Long id) {

        // TODO: 식별자로 job 엔티티 조회
        // TODO: job 엔티티에 저장된 빌드유형(buildType)에 따른 책임부여

    }





}
