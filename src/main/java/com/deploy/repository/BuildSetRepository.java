package com.deploy.repository;

import com.deploy.entity.BuildConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildSetRepository extends JpaRepository<BuildConfig, Long> {
}
