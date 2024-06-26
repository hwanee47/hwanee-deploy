package com.deploy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.aes.secret-key}")
    private String secretKey;
    @Value("${app.aes.salt}")
    private String salt;

    @Bean
    public AesBytesEncryptor aesBytesEncryptor() {
        return new AesBytesEncryptor(secretKey, salt);
    }


}
