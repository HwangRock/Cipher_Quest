package com.example.cipherquest.utils.cipher;


import org.springframework.stereotype.Component;
import java.security.SecureRandom;

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

    public String createRandomKey(){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder key=new StringBuilder();
        int keyLen = secureRandom.nextInt(10)+5;
        for(int i=0;i<keyLen;i++){
            int n=secureRandom.nextInt(26);
            key.append((char)n+'a');
        }
        return key.toString();
    }
}
