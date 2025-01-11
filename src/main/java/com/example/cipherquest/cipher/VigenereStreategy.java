package com.example.cipherquest.cipher;


import org.springframework.stereotype.Component;

@Component("stage2")
public class VigenereStreategy implements EncryptStrategy {
    public String encrypt(String plainText, String key) {
        plainText = plainText.toLowerCase();

        StringBuilder encryptedText = new StringBuilder();
        int fin = plainText.length();
        int keyLen = key.length();

        System.out.println("PlainText: " + plainText);
        System.out.println("Key: " + key);

        for (int i = 0; i < fin; i++) {
            char plainChar = plainText.charAt(i);
            if (plainChar >= 'a' && plainChar <= 'z') {
                int plainIdx = plainChar - 'a';
                char keyChar = key.charAt(i % keyLen);
                int keyIdx = keyChar - 'a';
                int encryptChar = (plainIdx + keyIdx) % 26;

                System.out.println("plainChar: " + plainChar + ", plainIdx: " + plainIdx + ", keyIdx: " +keyIdx);

                encryptedText.append((char) (encryptChar + 'a'));
            } else {
                encryptedText.append(plainChar);
            }
        }

        return encryptedText.toString();
    }




    public String decrypt(String cipherText, String key) {
        return "test Vigenere";
    }
}
