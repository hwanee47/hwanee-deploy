package com.deploy.repository;

import com.deploy.dto.request.CredentialSearchCond;
import com.deploy.dto.request.JobSearchCond;
import com.deploy.entity.Credential;
import com.deploy.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CredentialCustomRepository {
    Page<Credential> search(CredentialSearchCond condition, Pageable pageable);
}
