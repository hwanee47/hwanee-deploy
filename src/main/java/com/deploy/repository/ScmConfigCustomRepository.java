package com.deploy.repository;

import com.deploy.dto.request.JobSearchCond;
import com.deploy.dto.request.ScmConfigSearchCond;
import com.deploy.entity.Job;
import com.deploy.entity.ScmConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScmConfigCustomRepository {
    Page<ScmConfig> search(ScmConfigSearchCond condition, Pageable pageable);
}
