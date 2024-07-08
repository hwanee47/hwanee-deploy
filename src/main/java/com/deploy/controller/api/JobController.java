package com.deploy.controller.api;

import com.deploy.dto.CustomPageable;
import com.deploy.dto.request.*;
import com.deploy.dto.response.CredentialRes;
import com.deploy.dto.response.JobRes;
import com.deploy.dto.response.TuiGridRes;
import com.deploy.dto.response.handler.ResponseHandler;
import com.deploy.service.CredentialService;
import com.deploy.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;


    @GetMapping("/{id}")
    public ResponseEntity<?> findJob(@PathVariable Long id) {
        JobRes jobRes = jobService.findJob(id);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", jobRes);
    }

    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody @Valid JobCreateReq request) {
        Long id = jobService.createJob(request);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody @Valid JobUpdateReq request) {
        jobService.updateJob(id, request);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }

    @PostMapping("/run/{id}")
    public ResponseEntity<?> runJob(@PathVariable Long id) {
        jobService.runJob(id);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }
}
