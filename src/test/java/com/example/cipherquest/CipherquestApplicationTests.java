package com.example.cipherquest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CipherquestApplicationTests {

	@Test
	void contextLoads() {
		String plainText = "hello";
		String key = "key";

		String result = encrypt(plainText, key);

		System.out.println("Encrypted text: " + result);
	}

	public String encrypt(String plainText, Object key) {
		StringBuilder encryptedText = new StringBuilder();
		int fin=plainText.length();

		String k=(String)key;
		int keyLen=k.length();
		StringBuilder encryptKeyBuilder=new StringBuilder();

		for(int i=0;i<fin;i++){ // 키 생성
			char c=k.charAt(i%keyLen);
			encryptKeyBuilder.append(c);
		}
		String encryptKey=encryptKeyBuilder.toString();

		for(int i=0;i<fin;i++){ // 키와 평문을 매칭시켜서 암호화
			char plainChar=plainText.charAt(i);
			int plainIdx=plainChar-'a';

			char keyChar=encryptKey.charAt(i);
			int keyIdx=keyChar-'a';

			int encryptChar=(plainIdx+keyIdx)%26;

			encryptedText.append((char)(encryptChar+'a'));
		}

		return encryptedText.toString();
	}
}
