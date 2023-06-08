package com.shinhan.assetManager.user;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

// 비밀번호 암호화를 위한 클래스
// 단방향 해시 함수 (One-way Hash Function) 사용
// ex. AES256
// 참조 : https://wildeveloperetrain.tistory.com/98
@Component
public class AES256 {

	public static void main(String[] args) throws Exception {
		AES256 en = new AES256();
		 
		// 암호화
		String text = "암호화 하고 싶은 텍스트?";
		String encryptedText = en.encryptAES256(text);
		System.out.println(encryptedText);
		
		// 복호화
		String decryptedText = en.decryptAES256(encryptedText);
		System.out.println(decryptedText);
		
	}
	
	public static String alg = "AES/CBC/PKCS5Padding";
    private final String key = "abcdefghabcdefghabcdefghabcdefgh"; // 32byte
    private String iv = "0123456789abcdef"; // 16byte
	
	// 암호화
    public String encryptAES256(String text) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // 복호화
    public String decryptAES256(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, "UTF-8");
    }

}
