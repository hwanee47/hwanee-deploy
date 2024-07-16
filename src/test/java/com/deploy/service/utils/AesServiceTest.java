package com.deploy.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AesServiceTest {

    @Autowired
    AesService aesService;


    @Test
    public void 암복호화() {
        //given
        String plainText = "화니화니a123";

        //when
        String encrypt = aesService.encrypt(plainText);
        String decrypt = aesService.decrypt(encrypt);
        log.info("plainText={} -> encrypt={} -> decrypt={}", plainText, encrypt, decrypt);

        //then
        assertThat(decrypt).isEqualTo(plainText);

    }

    @Test
    public void 암복호화2() {
        //given
        String encrypt = "0BX0wT/1T18EwIm7NyWRTfvaAv6KhPWyDYj8qf+nhbI=";

        //when
        String decrypt = aesService.decrypt(encrypt);
        log.info("encrypt={} -> decrypt={}", encrypt, decrypt);


    }



}