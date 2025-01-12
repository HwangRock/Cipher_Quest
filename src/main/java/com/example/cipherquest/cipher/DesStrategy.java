package com.example.cipherquest.cipher;

import org.springframework.stereotype.Component;

@Component("stage8")
public class DesStrategy implements EncryptStrategy{
    public String encrypt(String plainText, String key) {
        return "test DES encryption";
    }

    public String decrypt(String cipherText, String key) {
        return "test DES decryption";
    }
}
