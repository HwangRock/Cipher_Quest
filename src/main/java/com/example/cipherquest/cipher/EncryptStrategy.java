package com.example.cipherquest.cipher;

public interface EncryptStrategy {
    String encrypt(String plainText, Object key);
    String decrypt(String cipherText, Object key);

}
