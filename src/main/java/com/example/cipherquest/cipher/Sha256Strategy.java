package com.example.cipherquest.cipher;

import org.springframework.stereotype.Component;

@Component("stage7")
public class Sha256Strategy implements EncryptStrategy{
    public String encrypt(String plainText, String key) {
        return "test SHA-256 encryption";
    }

    public String decrypt(String cipherText, String key) {
        return "test SHA-256 decryption";
    }

    public String createRandomKey(){
        return "key";
    }
}
