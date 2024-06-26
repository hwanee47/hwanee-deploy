package com.deploy.service;

import com.deploy.dto.request.CredentialCreateReq;
import com.deploy.entity.Credential;
import com.deploy.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CredentialService {

    private final CredentialRepository credentialRepository;

    @Transactional
    public Long save(CredentialCreateReq credentialCreateReq) {
        Credential entity = credentialCreateReq.toEntity();
        credentialRepository.save(entity);

        return entity.getId();
    }
}
