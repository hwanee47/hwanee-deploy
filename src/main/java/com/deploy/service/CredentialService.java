package com.deploy.service;

import com.deploy.dto.request.CredentialCreateReq;
import com.deploy.dto.request.CredentialSearchCond;
import com.deploy.dto.request.CredentialUpdateReq;
import com.deploy.dto.response.CredentialRes;
import com.deploy.entity.Credential;
import com.deploy.exception.AppBizException;
import com.deploy.repository.CredentialRepository;
import com.deploy.service.utils.AesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CredentialService {

    private final CredentialRepository credentialRepository;
    private final AesService aesService;


    public Page<CredentialRes> search(CredentialSearchCond condition, Pageable pageable) {
        Page<Credential> list = credentialRepository.search(condition, pageable);

        Page<CredentialRes> results = new PageImpl<>(
                list.stream().map(Credential -> {
                    CredentialRes credentialRes = new CredentialRes(Credential);
                    return credentialRes;
                }).collect(Collectors.toList())
                , pageable
                , list.getTotalElements()
        );

        return results;
    }

    /**
     * Credential 조회
     * @param id
     * @return
     */
    public CredentialRes findCredential(Long id) {

        Credential findCredential = credentialRepository.findById(id)
                .orElseThrow(() -> new AppBizException("No such data in Credential."));

        return CredentialRes.builder()
                .name(findCredential.getIdentifierName())
                .username(findCredential.getTargetUsername())
                .password(aesService.decrypt(findCredential.getTargetPassword()))
                .host(findCredential.getTargetHost())
                .port(findCredential.getTargetPort())
                .privateKey(aesService.decrypt(findCredential.getPrivateKey()))
                .passphrase(aesService.decrypt(findCredential.getPassphrase()))
                .description(findCredential.getDescription())
                .build();

    }

    /**
     * Credential 추가
     * @param request
     * @return
     */
    @Transactional
    public Long createCredential(CredentialCreateReq request) {

        String identifierName = request.getName();
        String username = request.getUsername();
        String password = request.getPassword();
        String host = request.getHost();
        Integer port = request.getPort();
        String privateKey = request.getPrivateKey();
        String passphrase = request.getPassphrase();
        String description = request.getDescription();

        Credential credential = Credential.createCredential(
                identifierName, username, aesService.encrypt(password),
                host, port, aesService.encrypt(privateKey),
                aesService.encrypt(passphrase), description);

        // save
        credentialRepository.save(credential);

        return credential.getId();
    }


    /**
     * Credential 수정
     * @param id
     * @param request
     * @return
     */
    @Transactional
    public Long updateCredential(Long id, CredentialUpdateReq request) {

        String identifierName = request.getName();
        String username = request.getUsername();
        String password = request.getPassword();
        String host = request.getHost();
        Integer port = request.getPort();
        String privateKey = request.getPrivateKey();
        String passphrase = request.getPassphrase();
        String description = request.getDescription();

        Credential findCredential = credentialRepository.findById(id)
                .orElseThrow(() -> new AppBizException("No such data in Credential."));


        // update
        findCredential.changeInfo(
                identifierName, username, aesService.encrypt(password),
                host, port, aesService.encrypt(privateKey),
                aesService.encrypt(passphrase), description);


        return id;
    }


    /**
     * Credential 삭제
     * @param id
     */
    @Transactional
    public void deleteCredential(Long id) {
        Credential findCredential = credentialRepository.findById(id)
                .orElseThrow(() -> new AppBizException("No such data in Credential."));

        credentialRepository.delete(findCredential);
    }
}
