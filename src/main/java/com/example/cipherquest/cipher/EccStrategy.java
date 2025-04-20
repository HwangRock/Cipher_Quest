package com.example.cipherquest.cipher;

import org.springframework.stereotype.Component;

@Component("stage12")
public class EccStrategy implements EncryptStrategy{
    public String encrypt(String plainText, String key) {
        return "test ECC encryption";
    }

    public String decrypt(String cipherText, String key) {
        return "test ECC decryption";
    }

    public String createRandomKey(){
        return "key";
    }
}
