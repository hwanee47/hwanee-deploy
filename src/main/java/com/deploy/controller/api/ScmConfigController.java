package com.deploy.controller.api;

import com.deploy.dto.CustomPageable;
import com.deploy.dto.request.*;
import com.deploy.dto.response.CredentialRes;
import com.deploy.dto.response.ScmConfigRes;
import com.deploy.dto.response.TuiGridRes;
import com.deploy.dto.response.handler.ResponseHandler;
import com.deploy.service.CredentialService;
import com.deploy.service.ScmConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/scmConfig")
@RequiredArgsConstructor
public class ScmConfigController {

    private final ScmConfigService scmConfigService;


    @GetMapping
    public TuiGridRes search(ScmConfigSearchCond condition, CustomPageable customPageable) {
        Page<ScmConfigRes> list = scmConfigService.search(condition, customPageable);
        return new TuiGridRes(list.getContent(), customPageable.getPage(), (int) list.getTotalElements(), customPageable.getPerPage());
    }

    @GetMapping("/searchAll")
    public ResponseEntity<?> searchAll() {
        List<ScmConfigRes> list = scmConfigService.searchAll();
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findScmConfig(@PathVariable Long id) {
        ScmConfigRes scmConfigRes = scmConfigService.findScmConfig(id);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", scmConfigRes);
    }

    @PostMapping
    public ResponseEntity<?> createScmConfig(@RequestBody @Valid ScmConfigCreateReq request) {
        Long id = scmConfigService.createScmConfig(request);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateScmConfig(@PathVariable Long id, @RequestBody @Valid ScmConfigUpdateReq request) {
        scmConfigService.updateScmConfig(id, request);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteScmConfig(@PathVariable Long id) {
        scmConfigService.deleteScmConfig(id);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }

    @PostMapping("/healthCheck")
    public ResponseEntity<?> healthCheck(@RequestBody @Valid ExecuteConntectReq request) {
        String resultMessage = scmConfigService.healthCheck(request);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", resultMessage);
    }
}
