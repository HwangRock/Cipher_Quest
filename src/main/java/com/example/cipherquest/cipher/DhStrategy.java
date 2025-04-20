package com.example.cipherquest.cipher;

import org.springframework.stereotype.Component;

@Component("stage11")
public class DhStrategy implements EncryptStrategy {
    public String encrypt(String plainText, String key) {
        return "test DH encrypt";
    }

    public String decrypt(String cipherText, String key) {
        return "test DH decrypt";
    }

    public String createRandomKey(){
        return "key";
    }
}
