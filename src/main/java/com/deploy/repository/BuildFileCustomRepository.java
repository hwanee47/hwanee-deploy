package com.deploy.repository;

import com.deploy.entity.BuildFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BuildFileCustomRepository {
    Page<BuildFile> searchByJobId(Long jobId, Pageable pageable);
}
