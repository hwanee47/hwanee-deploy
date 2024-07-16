package com.deploy.repository;

import com.deploy.dto.request.JobSearchCond;
import com.deploy.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobCustomRepository {
    Page<Job> search(JobSearchCond condition, Pageable pageable);
}
