package com.deploy.repository;

import com.deploy.entity.RunHistory;
import com.deploy.entity.RunHistoryDetail;
import com.deploy.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleCustomRepository {
    Page<Schedule> searchByJobId(Long jobId, Pageable pageable);
}
