package com.deploy.repository;

import com.deploy.entity.CodeManageConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeManageConfigRepository extends JpaRepository<CodeManageConfig, Long> {
}
