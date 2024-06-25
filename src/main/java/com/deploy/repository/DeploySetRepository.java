package com.deploy.repository;

import com.deploy.entity.DeployConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeploySetRepository extends JpaRepository<DeployConfig, Long> {
}
