package com.example.cipherquest.cipher;


import org.springframework.stereotype.Component;

@Component("stage3")
public class PlayfairStrategy implements EncryptStrategy{
    public String encrypt(String plainText, Object key) {
        return "test playfair";
    }

    public String decrypt(String cipherText, Object key) {
        return "test playfair";
    }
}
