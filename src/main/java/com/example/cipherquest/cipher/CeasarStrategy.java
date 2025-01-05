package com.example.cipherquest.cipher;

import org.springframework.stereotype.Component;

@Component("stage1")
public class CeasarStrategy implements EncryptStrategy{
    public String encrypt(String plainText, Object key){
        plainText=plainText.toLowerCase();
        int fin=plainText.length();
        StringBuilder cipherText=new StringBuilder();
        int k=(int)key;

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

    public String decrypt(String cipherText, Object key){
        return "test ceasar decryption";
    }
}
