package com.example.cipherquest.cipher;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component("stage3")
public class PlayfairStrategy implements EncryptStrategy{
    public String encrypt(String plainText, String key) {
        // 플레이페어 키 테이블 생성
        Character[][] table = generatePlayfairTable(key);

        // 알파벳과 비알파벳 분리
        List<Character> plainAlphabets = new ArrayList<>();
        List<int[]> nonAlphabetPositions = new ArrayList<>();
        int plainTextSize=plainText.length();

        for (int i = 0; i < plainTextSize; i++) {
            char ch = plainText.charAt(i);
            if (Character.isLetter(ch)) {
                plainAlphabets.add(Character.toLowerCase(ch == 'i' ? 'j' : ch)); // i 대신에 j만 사용.
            } else {
                nonAlphabetPositions.add(new int[]{i, ch});
            }
        }

        int plainAlphabetsSize = plainAlphabets.size();
        int start = nonAlphabetPositions.size() - 1;
        int before = 0;

        // 뒤에 추가됐을때 비알파벳 위치 조정
        if (plainAlphabetsSize % 2 == 1 || nonAlphabetPositions.get(start)[0] == plainTextSize) {
            for (int i = start; i >= 0; i--) {
                if (i == start) {
                    before = plainTextSize-1;
                    nonAlphabetPositions.get(i)[0] += 1;
                }
                else if (nonAlphabetPositions.get(i)[0] == before - 1) {
                    before = nonAlphabetPositions.get(i)[1];
                    nonAlphabetPositions.get(i)[0] += 1;
                }
                else{
                    break;
                }
            }
        }

        // 알파벳 그룹 암호화
        StringBuilder cipherAlphabets = encryptPlayfair(plainAlphabets, table);

        // 비알파벳 위치 복원
        StringBuilder cipherText = new StringBuilder(cipherAlphabets);
        for (int[] pos : nonAlphabetPositions) {
            cipherText.insert(pos[0], (char) pos[1]);
        }

        return cipherText.toString();
    }

    // 플레이페어 키 테이블 생성
    public static Character[][] generatePlayfairTable(String key) {
        key = key.toLowerCase();
        StringBuilder noOverlapB = new StringBuilder();
        HashMap<Character, Boolean> m = new HashMap<>();

        // 키 중복 제거
        int keySize = key.length();
        for(int i = 0; i < keySize; i++) {
            char ch = key.charAt(i);
            if(ch=='i') {
                ch = 'j';
            }
            if(!m.containsKey(ch) && Character.isLetter(ch)){
                noOverlapB.append(ch);
                m.put(ch, true);
            }
        }

        Character[][] table = new Character[5][5];
        String noOverlap = noOverlapB.toString();

        int noOverlapSize = noOverlap.length();
        for(int i = 0; i < noOverlapSize; i++) {
            char ch = noOverlap.charAt(i);
            table[i / 5][i % 5] = ch;
        }

        // 나머지 문자 추가
        int index = noOverlapSize;
        char cur = 'a';
        while (index < 25) {
            if (cur == 'i'){
                cur = 'j';
            }
            if (!m.containsKey(cur)) {
                table[index / 5][index % 5] = cur;
                m.put(cur, true);
                index++;
            }
            cur++;
        }
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }
        return table;
    }

    // 플레이페어 암호화
    public static StringBuilder encryptPlayfair(List<Character> plainAlphabets, Character[][] table) {
        StringBuilder cipherTextB = new StringBuilder();
        int n = plainAlphabets.size();
        if (n % 2 != 0) plainAlphabets.add('x'); // 홀수면 x 추가

        for (int i = 0; i < n; i += 2) {
            char fir = plainAlphabets.get(i);
            char sec = plainAlphabets.get(i + 1);
            if (fir == sec) sec = 'x';

            int[] firPos = findPosition(fir, table);
            int[] secPos = findPosition(sec, table);

            if (firPos[0] == secPos[0]) { // 같은 행
                firPos[1] = (firPos[1] + 1) % 5;
                secPos[1] = (secPos[1] + 1) % 5;
            }
            else if (firPos[1] == secPos[1]) { // 같은 열
                firPos[0] = (firPos[0] + 1) % 5;
                secPos[0] = (secPos[0] + 1) % 5;
            }
            else { // 직사각형
                int temp = firPos[1];
                firPos[1] = secPos[1];
                secPos[1] = temp;
            }
            cipherTextB.append(table[firPos[0]][firPos[1]]);
            cipherTextB.append(table[secPos[0]][secPos[1]]);
        }
        return cipherTextB;
    }

    // 테이블에서 문자의 위치 찾기
    public static int[] findPosition(char ch, Character[][] table) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (table[i][j] == ch) return new int[]{i, j};
            }
        }
        return null;
    }

    public String decrypt(String cipherText, String key) {
        return "test playfair";
    }
}
