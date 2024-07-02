package com.deploy.controller.api;

import com.deploy.dto.CustomPageable;
import com.deploy.dto.request.CredentialCreateReq;
import com.deploy.dto.request.CredentialSearchCond;
import com.deploy.dto.request.CredentialUpdateReq;
import com.deploy.dto.response.CredentialRes;
import com.deploy.dto.response.TuiGridRes;
import com.deploy.dto.response.handler.ResponseHandler;
import com.deploy.service.CredentialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/credential")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialService credentialService;


    @GetMapping
    public TuiGridRes search(CredentialSearchCond condition, CustomPageable customPageable) {
        Page<CredentialRes> list = credentialService.search(condition, customPageable);
        return new TuiGridRes(list.getContent(), customPageable.getPage(), (int) list.getTotalElements(), customPageable.getPerPage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCredential(@PathVariable Long id) {
        CredentialRes credentialRes = credentialService.findCredential(id);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", credentialRes);
    }

    @PostMapping
    public ResponseEntity<?> createCredential(@RequestBody @Valid CredentialCreateReq credentialCreateReq) {
        Long id = credentialService.createCredential(credentialCreateReq);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCredential(@PathVariable Long id, @RequestBody @Valid CredentialUpdateReq credentialUpdateReq) {
        credentialService.updateCredential(id, credentialUpdateReq);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCredential(@PathVariable Long id) {
        credentialService.deleteCredential(id);
        return ResponseHandler.generateResponse(HttpStatus.OK, "success", id);
    }
}
