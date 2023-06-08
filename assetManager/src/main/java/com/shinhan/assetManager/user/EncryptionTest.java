package com.shinhan.assetManager.user;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionTest {

	public static void main(String[] args) throws NoSuchAlgorithmException {

		SHA256 en = new SHA256();
		String ssn = "9900111234567"; // 주민번호
		String pw = "12345678"; // 비번
		
		// Salt 생성
		String salt = en.getSalt(); // 매번 다른값 생성됨 (난수 이용했으므로) 
		System.out.println("salt : " + salt);
		
		// 최종 ssn 생성
		String result1 = en.getEncrypt(ssn, salt);

		// 최종 pw 생성
		String result2 = en.getEncrypt(pw, salt);
		
		System.out.println(result1);
		System.out.println(result2);
	}

}
