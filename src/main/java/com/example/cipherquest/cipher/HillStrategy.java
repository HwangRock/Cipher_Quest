package com.example.cipherquest.cipher;

import org.springframework.stereotype.Component;

@Component("stage4")
public class HillStrategy implements EncryptStrategy{
    public String encrypt(String plainText, Object key) {
        return "test hill";
    }

    public String decrypt(String cipherText, Object key) {
        return "test hill";
    }
}
