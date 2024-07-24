package com.deploy.repository;

import com.deploy.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleCustomRepository{
    // 시스템 시간을 기준으로 예정된 스케줄 데이터 조회
    List<Schedule> findByExecutedFalseAndScheduleTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
