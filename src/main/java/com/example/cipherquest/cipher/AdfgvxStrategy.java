package com.example.cipherquest.cipher;

import org.springframework.stereotype.Component;

@Component("stage6")
public class AdfgvxStrategy implements EncryptStrategy{
    public String encrypt(String plainText, String key) {

        return "test ADGFGVX encrypt";
    }
    public String decrypt(String cipherText, String key) {

        return "test ADGFGVX decrypt";
    }
    public String createRandomKey(){
        return "key";
    }

}
