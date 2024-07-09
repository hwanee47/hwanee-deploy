package com.deploy.repository;

import com.deploy.entity.RunHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RunHistoryRepository extends JpaRepository<RunHistory, Long>, RunHistoryCustomRepository{
    @Query(value = "select max(rh.jobRunSeq) from RunHistory rh where rh.job.id = :jobId")
    Long findMaxSeq(Long jobId);
}
