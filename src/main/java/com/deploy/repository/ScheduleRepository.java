package com.deploy.repository;

import com.deploy.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleCustomRepository{
    // 현재시간보다 작거나 같은 스케줄 조회
    List<Schedule> findByExecutedFalseAndScheduleTimeLessThanEqual(LocalDateTime dateTime);
}
