package com.example.cipherquest.utils.cipher;

import org.springframework.stereotype.Component;

@Component("stage8")
public class DesStrategy implements EncryptStrategy{
    public String encrypt(String plainText, String key) {
        return "test DES encryption";
    }

    public String decrypt(String cipherText, String key) {
        return "test DES decryption";
    }

    public String createRandomKey(){
        return "key";
    }
}
