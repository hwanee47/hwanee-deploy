package com.deploy.service;

import com.deploy.dto.request.CredentialCreateReq;
import com.deploy.entity.Credential;
import com.deploy.repository.CredentialRepository;
import com.deploy.service.utils.AesService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CredentialServiceTest {

    @Autowired
    CredentialService credentialService;

    @Autowired
    CredentialRepository credentialRepository;

    @Autowired
    AesService aesService;


    @Test
    public void 등록_테스트() {
        //given
        CredentialCreateReq credentialCreateReq = new CredentialCreateReq();
        credentialCreateReq.setName("credential-1");
        credentialCreateReq.setUsername("hwaneehwanee");
        credentialCreateReq.setPassword("password");
        credentialCreateReq.setPrivateKey("privateKey");

        //when
        Long id = credentialService.createCredential(credentialCreateReq);
        Credential findCredential = credentialRepository.findById(id).get();

        //then
        assertThat(credentialCreateReq.getName()).isEqualTo(findCredential.getIdentifierName());
        assertThat(credentialCreateReq.getPassword()).isEqualTo(aesService.decrypt(findCredential.getTargetPassword()));

    }

}