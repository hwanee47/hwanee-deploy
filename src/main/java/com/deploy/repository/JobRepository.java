package com.deploy.repository;

import com.deploy.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long>, JobCustomRepository {
}
