package com.deploy.repository;

import com.deploy.dto.request.JobSearchCond;
import com.deploy.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StepCustomRepository {
    Long maxStepIndex(Long jobId);
}
