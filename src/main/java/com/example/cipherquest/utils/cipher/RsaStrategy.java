package com.example.cipherquest.utils.cipher;

import org.springframework.stereotype.Component;

@Component("stage10")
public class RsaStrategy implements EncryptStrategy{
    public String encrypt(String plainText, String key) {
        return "test RSA encrypt";
    }

    public String decrypt(String cipherText, String key) {
        return "test RSA decrypt";
    }

    public String createRandomKey(){
        return "key";
    }
}
