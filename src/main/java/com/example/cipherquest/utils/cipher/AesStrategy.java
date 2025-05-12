package com.example.cipherquest.utils.cipher;

import org.springframework.stereotype.Component;

@Component("stage9")
public class AesStrategy implements EncryptStrategy {
    public String encrypt(String plainText, String key) {
        return "test AES encryption";
    }

    public String decrypt(String cipherText, String key) {
        return "test AES decryption";
    }
    public String createRandomKey(){
        return "key";
    }
}
