package com.deploy.controller.api;


import com.deploy.dto.CustomPageable;
import com.deploy.dto.request.ScheduleCreateReq;
import com.deploy.dto.request.ScheduleUpdateReq;
import com.deploy.dto.response.RunHistoryRes;
import com.deploy.dto.response.ScheduleRes;
import com.deploy.dto.response.TuiGridRes;
import com.deploy.dto.response.handler.ResponseHandler;
import com.deploy.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/project/{jobId}")
    public TuiGridRes searchByJobId(@PathVariable Long jobId, CustomPageable customPageable) {
        Page<ScheduleRes> list = scheduleService.searchByJobId(jobId, customPageable);
        return new TuiGridRes(list.getContent(), customPageable.getPage(), (int) list.getTotalElements(), customPageable.getPerPage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findSchedule(@PathVariable Long id) {
        ScheduleRes scheduleRes = scheduleService.findSchedule(id);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", scheduleRes);
    }

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody @Valid ScheduleCreateReq request) {
        Long id = scheduleService.createSchedule(request);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long id, @RequestBody @Valid ScheduleUpdateReq request) {
        scheduleService.updateSchedule(id, request);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }



}
