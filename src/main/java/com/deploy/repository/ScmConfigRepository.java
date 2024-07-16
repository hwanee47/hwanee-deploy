package com.deploy.repository;

import com.deploy.entity.ScmConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScmConfigRepository extends JpaRepository<ScmConfig, Long>, ScmConfigCustomRepository {
}
