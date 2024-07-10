package com.deploy.controller.api;

import com.deploy.dto.CustomPageable;
import com.deploy.dto.request.BuildFileUpdateReq;
import com.deploy.dto.response.BuildFileRes;
import com.deploy.dto.response.RunHistoryDetailRes;
import com.deploy.dto.response.RunHistoryRes;
import com.deploy.dto.response.TuiGridRes;
import com.deploy.dto.response.handler.ResponseHandler;
import com.deploy.service.BuildFileService;
import com.deploy.service.RunHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/api/buildFile")
@RequiredArgsConstructor
public class BuildFileController {

    private final BuildFileService buildFileService;

    @GetMapping("/list/{jobId}")
    public TuiGridRes searchByJobId(@PathVariable Long jobId, CustomPageable customPageable) {
        Page<BuildFileRes> list = buildFileService.searchByJobId(jobId, customPageable);
        return new TuiGridRes(list.getContent(), customPageable.getPage(), (int) list.getTotalElements(), customPageable.getPerPage());
    }

    @GetMapping("/{buildFileId}")
    public ResponseEntity<?> findBuildFile(@PathVariable Long buildFileId) {
        BuildFileRes buildFileRes = buildFileService.findBuildFile(buildFileId);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", buildFileRes);
    }


    @PutMapping("/{buildFileId}")
    public ResponseEntity<?> updateBuildFile(@PathVariable Long buildFileId, @RequestBody @Valid BuildFileUpdateReq request) {
        buildFileService.updateBuildFile(buildFileId, request);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", buildFileId);
    }

}
