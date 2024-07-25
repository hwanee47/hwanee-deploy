package com.deploy.controller.api;

import com.deploy.dto.request.CredentialCreateReq;
import com.deploy.dto.request.StepCreateReq;
import com.deploy.dto.request.StepToggleReq;
import com.deploy.dto.request.StepUpdateReq;
import com.deploy.dto.response.handler.ResponseHandler;
import com.deploy.service.StepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/step")
@RequiredArgsConstructor
public class StepController {

    private final StepService stepService;

    @PostMapping
    public ResponseEntity<?> createStep(@RequestBody @Valid StepCreateReq request) {
        Long id = stepService.createStep(request);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStep(@PathVariable Long id, @RequestBody @Valid StepUpdateReq request) {
        stepService.updateStep(id, request);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }


    @PutMapping("/{id}/toggle")
    public ResponseEntity<?> toggle(@PathVariable Long id, @RequestBody @Valid StepToggleReq request) {
        stepService.toggleSwitch(id, request);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }
}
