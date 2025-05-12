package com.example.cipherquest.utils.cipher;

public interface EncryptStrategy {
    String encrypt(String plainText, String key);
    String decrypt(String cipherText, String key);
    String createRandomKey();
}
