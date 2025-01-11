package com.example.cipherquest.cipher;

public interface EncryptStrategy<k> {
    String encrypt(String plainText, k key);
    String decrypt(String cipherText, k key);

}
