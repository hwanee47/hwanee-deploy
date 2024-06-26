package com.deploy.service;

import com.deploy.dto.request.JobCreateReq;
import com.deploy.entity.Job;
import com.deploy.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobService {

    private final StepService service;
    private final JobRepository jobRepository;

    @Transactional
    public Long save(JobCreateReq jobCreateReq) {
        Job entity = jobCreateReq.toEntity();
        jobRepository.save(entity);

        return entity.getId();
    }


    // CI/CD 시작
    @Transactional
    public void run(Long id) {

        // TODO: 식별자로 job 엔티티 조회
        // TODO: job 엔티티에 저장된 빌드유형(buildType)에 따른 책임부여

    }





}
