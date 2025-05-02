package com.example.cipherquest.cipher;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.HashMap;

@Component("stage6")
public class AdfgvxStrategy implements EncryptStrategy{

    public String encrypt(String plainText, String key) {

        StringBuilder encryptedTxt=new StringBuilder();
        StringBuilder substitutedTxt=new StringBuilder();
        HashMap<Character,String>substitution=new HashMap<>();
        HashMap<Integer,Character>adf=new HashMap<>();
        char board[][]={
                {'1','4','7','R','E','G'},
                {'I','M','N','T','A','B'},
                {'C','D','F','H','J','K'},
                {'L','O','P','Q','S','U'},
                {'V','W','X','Y','Z','0'},
                {'2','3','5','6','8','9'}
        };

        adf.put(0,'A');
        adf.put(1,'D');
        adf.put(2,'F');
        adf.put(3,'G');
        adf.put(4,'V');
        adf.put(5,'X');

        for(int i=0;i<6;i++){
            char a=adf.get(i);
            for(int j=0;j<6;j++){
                char b=adf.get(j);
                StringBuilder sb=new StringBuilder(a+b);
                substitution.put(board[i][j],sb.toString());
            }
        }

        int fin=plainText.length();
        for(int i=0;i<fin;i++){
            char c=plainText.charAt(i);
            String sub=substitution.get(c);
            substitutedTxt.append(sub);
        }

        int ln=substitutedTxt.length();
        int keyLen=key.length();
        if(ln%keyLen!=0){
            int paddingNum=ln%keyLen;
            for(int i=0;i<paddingNum;i++){
                substitutedTxt.append('X');
            }
        }

        int col=ln/keyLen;
        String st[][]=new String[col][keyLen];
        

        return "test ADGFGVX encrypt";
    }

    public String decrypt(String cipherText, String key) {

        return "test ADGFGVX decrypt";
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
