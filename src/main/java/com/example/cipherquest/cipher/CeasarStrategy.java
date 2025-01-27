package com.example.cipherquest.cipher;

import org.springframework.stereotype.Component;

@Component("stage1")
public class CeasarStrategy implements EncryptStrategy{
    public String encrypt(String plainText, String key){
        plainText=plainText.toLowerCase();
        int fin=plainText.length();
        StringBuilder cipherText=new StringBuilder();

        int k;
        try {
            k = Integer.parseInt(key); // 문자열을 정수로 변환
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ceasar 암호의 키는 정수여야 합니다. 제공된 키: " + key);
        }

        for(int i=0;i<fin;i++){
            char ch=plainText.charAt(i);
            if(ch>='a' &&ch<='z'){
                cipherText.append((char) ((ch-'a'+k)%26+'a')); // 0~25로 만들고 k만큼 욺기고 다시 0~25로 만들고 아스키코드에 맞춤.
            }
            else{
                cipherText.append(ch); // 암호화가 필요없는 경우는 그대로 넣음.
            }
        }

        return cipherText.toString();
    }

    public String decrypt(String cipherText, String key){
        int fin=cipherText.length();
        int k=Integer.parseInt(key);
        StringBuilder plainText=new StringBuilder();
        for(int i=0;i<fin;i++){
            char ch=cipherText.charAt(i);
            if(ch>='a' &&ch<='z'){
                plainText.append((char) ((ch-'a'-k+26)%26+'a'));
            }
            else{
                plainText.append(ch);
            }
        }

        return plainText.toString();
    }
}
