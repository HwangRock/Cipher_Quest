package com.example.cipherquest.cipher;

public interface EncryptStrategy {
    String encrypt(final String plainText, final Object key);
    String decrypt(final String cipherText, final Object key);

}
