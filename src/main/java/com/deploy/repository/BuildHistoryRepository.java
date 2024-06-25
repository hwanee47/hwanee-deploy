package com.deploy.repository;

import com.deploy.entity.BuildHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildHistoryRepository extends JpaRepository<BuildHistory, Long> {
}
