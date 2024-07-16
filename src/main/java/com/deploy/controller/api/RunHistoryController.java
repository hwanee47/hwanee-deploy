package com.deploy.controller.api;

import com.deploy.dto.CustomPageable;
import com.deploy.dto.response.RunHistoryDetailRes;
import com.deploy.dto.response.RunHistoryRes;
import com.deploy.dto.response.TuiGridRes;
import com.deploy.dto.response.handler.ResponseHandler;
import com.deploy.service.RunHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/api/runHistory")
@RequiredArgsConstructor
public class RunHistoryController {

    private final RunHistoryService runHistoryService;

    @GetMapping("/{jobId}")
    public TuiGridRes searchByJobId(@PathVariable Long jobId, CustomPageable customPageable) {
        Page<RunHistoryRes> list = runHistoryService.searchByJobId(jobId, customPageable);
        return new TuiGridRes(list.getContent(), customPageable.getPage(), (int) list.getTotalElements(), customPageable.getPerPage());
    }

    @GetMapping("/{jobId}/{historyId}")
    public TuiGridRes searchByHistoryId(@PathVariable Long jobId, @PathVariable Long historyId, CustomPageable customPageable) {
        Page<RunHistoryDetailRes> list = runHistoryService.searchByHistoryId(jobId, historyId, customPageable);
        return new TuiGridRes(list.getContent(), customPageable.getPage(), (int) list.getTotalElements(), customPageable.getPerPage());
    }


    @GetMapping("/{historyId}/log")
    public ResponseEntity<?> log(@PathVariable Long historyId) throws IOException {
        try {
            String logFilePath = runHistoryService.getLogFilePath(historyId);
            String content = StringUtils.hasText(logFilePath) ? Files.readString(Paths.get(logFilePath)) : "";
            return ResponseHandler.generateResponse(HttpStatus.OK, "success", content);
        } catch (IOException e) {
            log.error("Get log error. message={}", e.getMessage());
            throw e;
        }
    }
}
