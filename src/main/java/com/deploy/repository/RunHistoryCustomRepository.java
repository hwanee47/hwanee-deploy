package com.deploy.repository;

import com.deploy.entity.RunHistory;
import com.deploy.entity.RunHistoryDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RunHistoryCustomRepository {
    Page<RunHistory> searchByJobId(Long jobId, Pageable pageable);
    Page<RunHistoryDetail> searchByHistoryId(Long jobId, Long historyId, Pageable pageable);
}
