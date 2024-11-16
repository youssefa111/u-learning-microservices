package com.joe.lib.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
@Getter
public class GenerateSecretKeyConfig implements ApplicationRunner {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public void run(ApplicationArguments args) {
        if (secretKey.isEmpty()) {
            generateAndPrintSecretKey();
        }
    }

    private void generateAndPrintSecretKey() {
        // Generate a 512-bit (64-byte) random key
        byte[] keyBytes = new byte[64];
        new SecureRandom().nextBytes(keyBytes);

        // Convert the byte array to a hexadecimal string
        StringBuilder key = new StringBuilder();
        for (byte b : keyBytes) {
            key.append(String.format("%02x", b));
        }

        // Set the generated secret key in the properties
        secretKey = key.toString();
    }
}
