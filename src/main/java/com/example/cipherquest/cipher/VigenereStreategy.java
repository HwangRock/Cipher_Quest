package com.example.cipherquest.cipher;


import org.springframework.stereotype.Component;

@Component("stage2")
public class VigenereStreategy implements EncryptStrategy {
    public String encrypt(String plainText, Object key) {
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("Key must be a String. Actual type: " + key.getClass().getName());
        }

        String k = (String) key;
        StringBuilder encryptedText = new StringBuilder();
        int fin = plainText.length();
        int keyLen = k.length();
        StringBuilder encryptKeyBuilder = new StringBuilder();

        // 키 생성
        for (int i = 0; i < fin; i++) {
            char c = k.charAt(i % keyLen);
            encryptKeyBuilder.append(c);
        }

        String repeatedKey = encryptKeyBuilder.toString();

        // 키와 평문을 매칭시켜 암호화
        for (int i = 0; i < fin; i++) {
            char plainChar = plainText.charAt(i);
            int plainIdx = plainChar - 'a';

            char keyChar = repeatedKey.charAt(i);
            int keyIdx = keyChar - 'a';

            int encryptChar = (plainIdx + keyIdx) % 26;

            encryptedText.append((char) (encryptChar + 'a'));
        }

        return encryptedText.toString();
    }



    public String decrypt(String cipherText, Object key) {
        return "test Vigenere";
    }
}
