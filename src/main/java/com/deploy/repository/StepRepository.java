package com.deploy.repository;

import com.deploy.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepRepository extends JpaRepository<Step, Long>, StepCustomRepository {
}
