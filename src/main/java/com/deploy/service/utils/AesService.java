package com.deploy.service.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AesService {

    private final AesBytesEncryptor encryptor;

    /**
     * 암호화
     * @param plainText
     * @return
     */
    public String encrypt(String plainText) {
        byte[] encrypt = encryptor.encrypt(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypt);
    }

    /**
     * 복호화
     * @param encryptText
     * @return
     */
    public String decrypt(String encryptText) {
        byte[] decryptBytes = Base64.getDecoder().decode(encryptText);
        byte[] decrypt = encryptor.decrypt(decryptBytes);
        return new String(decrypt, StandardCharsets.UTF_8);
    }

}
