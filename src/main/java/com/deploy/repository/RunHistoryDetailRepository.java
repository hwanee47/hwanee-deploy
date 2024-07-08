package com.deploy.repository;

import com.deploy.entity.RunHistory;
import com.deploy.entity.RunHistoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RunHistoryDetailRepository extends JpaRepository<RunHistoryDetail, Long> {
}
