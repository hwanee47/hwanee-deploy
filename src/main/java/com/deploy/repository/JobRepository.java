package com.deploy.repository;

import com.deploy.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long>, JobCustomRepository {
    List<Job> findAllByCreatedByOrderByIdDesc(Long id);
}
