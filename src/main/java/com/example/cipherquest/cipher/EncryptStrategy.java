package com.example.cipherquest.cipher;

public interface EncryptStrategy {
    String encrypt(String plainText, String key);
    String decrypt(String cipherText, String key);

}
