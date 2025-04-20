package com.example.cipherquest.cipher;


import org.springframework.stereotype.Component;

@Component("stage5")
public class RailfenceStrategy implements EncryptStrategy {
    public String encrypt(String plainText, String key) {
        plainText=plainText.toLowerCase();
        int fin=plainText.length();
        StringBuilder cipherText=new StringBuilder();

        int k;
        try {
            k = Integer.parseInt(key); // 문자열을 정수로 변환
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ceasar 암호의 키는 정수여야 합니다. 제공된 키: " + key);
        }

        Character[][] rail=new Character[k][fin];
        int row=0;
        boolean down=true;
        for(int i=0;i<fin;i++){ // rail fence 작성
            char c=plainText.charAt(i);

            rail[row][i]=c;
            if(down){
                if(row==k-1){
                    down=false;
                    row--;
                }
                else{
                    row++;
                }
            }
            else{
                if(row==0){
                    down=true;
                    row++;
                }
                else{
                    row--;
                }
            }
        }

        for(int i=0;i<k;i++){
            for(int j=0;j<fin;j++){
                if(rail[i][j] != null){
                    cipherText.append(rail[i][j]);
                }
            }
        }

        return cipherText.toString();
    }

    public String decrypt(String cipherText, String key) {
        return "test rail fence";
    }

    public String createRandomKey(){
        return "key";
    }
}
