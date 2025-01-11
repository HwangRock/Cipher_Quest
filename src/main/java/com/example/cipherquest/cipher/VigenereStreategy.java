package com.example.cipherquest.cipher;


import org.springframework.stereotype.Component;

@Component("stage2")
public class VigenereStreategy implements EncryptStrategy {
    public String encrypt(String plainText, Object key) {
        return "test Vigenere";
    }


    public String decrypt(String cipherText, Object key) {
        return "test Vigenere";
    }
}
