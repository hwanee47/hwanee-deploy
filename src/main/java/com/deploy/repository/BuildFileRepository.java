package com.deploy.repository;

import com.deploy.entity.BuildFile;
import com.deploy.entity.RunHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BuildFileRepository extends JpaRepository<BuildFile, Long>, BuildFileCustomRepository{
}
