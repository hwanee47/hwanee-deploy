package com.deploy.controller.api;

import com.deploy.dto.CustomPageable;
import com.deploy.dto.request.BuildFileUpdateReq;
import com.deploy.dto.response.BuildFileRes;
import com.deploy.dto.response.TuiGridRes;
import com.deploy.dto.response.handler.ResponseHandler;
import com.deploy.service.BuildFileService;
import com.deploy.service.StepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/api/buildFile")
@RequiredArgsConstructor
public class BuildFileController {

    private final BuildFileService buildFileService;
    private final StepService stepService;

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

    @GetMapping("/download/{buildFileId}")
    public ResponseEntity<?> downloadBuildFile(@PathVariable Long buildFileId) {
        try {
            BuildFileRes buildFile = buildFileService.findBuildFile(buildFileId);
            String buildFilePath = buildFile.getBuildFilePath();
            Path path = Paths.get(buildFilePath);

            Resource resource = new UrlResource(path.toUri());

            if (resource.exists()) {
                String contentType = Files.probeContentType(path);

                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/deploy/{buildFileId}")
    public ResponseEntity<?> designateDeploy(@PathVariable Long buildFileId) throws Exception {
        stepService.designateDeploy(buildFileId);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", buildFileId);
    }

}
