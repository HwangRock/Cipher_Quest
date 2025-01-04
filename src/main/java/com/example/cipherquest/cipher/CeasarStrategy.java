package com.example.cipherquest.cipher;

import org.springframework.stereotype.Component;

@Component("stage1")
public class CeasarStrategy implements EncryptStrategy{
    public String encrypt(final String plainText, final Object key){
        return "test ceasar encrypt";
    }

    public String decrypt(final String cipherText, final Object key){
        return "test ceasar decryption";
    }
}
