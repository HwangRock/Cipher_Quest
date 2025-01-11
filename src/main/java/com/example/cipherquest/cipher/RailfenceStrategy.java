package com.example.cipherquest.cipher;


import org.springframework.stereotype.Component;

@Component("stage5")
public class RailfenceStrategy implements EncryptStrategy {
    public String encrypt(String plainText, Object key) {
        return "test rail fence";
    }

    public String decrypt(String cipherText, Object key) {
        return "test rail fence";
    }
}
