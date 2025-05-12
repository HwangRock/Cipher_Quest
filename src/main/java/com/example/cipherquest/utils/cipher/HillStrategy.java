package com.example.cipherquest.utils.cipher;

import org.springframework.stereotype.Component;

@Component("stage4")
public class HillStrategy implements EncryptStrategy{
    public String encrypt(String plainText, String key) {
        return "test hill";
    }

    public String decrypt(String cipherText, String key) {
        return "test hill";
    }

    public String createRandomKey(){
        return "key";
    }
}
