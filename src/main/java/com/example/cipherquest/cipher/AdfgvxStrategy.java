package com.example.cipherquest.cipher;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.Arrays;
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
        char st[][]=new char[col][keyLen];
        for(int i=0;i<fin;i++){
            int col2=i/keyLen;
            int row2=i%keyLen;
            char c=plainText.charAt(i);
            st[col2][row2]=c;
        }

        // key를 사전순으로 정렬시켜서 어떻게 순열.
        // hash를 이용해서 키를 정렬시킨 새로운 문자열과 키의 문자열의 문자를 매개로 해서 인덱스끼리 짝을 지음.

        // 위의 hash를 이용해서 st의 열 별로 순열시켜서 암호화 완성.

        HashMap<Character, Integer> keyIndexMap = new HashMap<>();
        for (int i = 0; i < keyLen; i++) {
            keyIndexMap.put(key.charAt(i), i);
        }

        char[] sortedKey = key.toCharArray();
        Arrays.sort(sortedKey);

        int[] permutation = new int[keyLen];
        boolean[] visited = new boolean[keyLen];
        for (int i = 0; i < keyLen; i++) {
            for (int j = 0; j < keyLen; j++) {
                if (!visited[j] && key.charAt(j) == sortedKey[i]) {
                    permutation[i] = j;
                    visited[j] = true;
                    break;
                }
            }
        }

        for (int i = 0; i < keyLen; i++) {
            int colIdx = permutation[i];
            for (int j = 0; j < col; j++) {
                encryptedTxt.append(st[j][colIdx]);
            }
        }

        return encryptedTxt.toString();
    }

    public String decrypt(String cipherText, String key) {

        return "test ADGFGVX decrypt";
    }

    public String createRandomKey(){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder key=new StringBuilder();
        HashMap<Integer,Boolean>h=new HashMap<>();
        int keyLen = secureRandom.nextInt(10)+5;
        for(int i=0;i<keyLen;i++){
            int n=0;
            while(true){
                n=secureRandom.nextInt(26);
                if(!h.containsKey(n)){
                    h.put(n,true);
                    break;
                }
            }
            key.append((char)n+'a');
        }
        return key.toString();
    }
}
