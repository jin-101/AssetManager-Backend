package com.shinhan.assetManager.user;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

public class Salt {
	
	public static String getSalt() {
		// 1. Random, byte 객체 생성
		SecureRandom r = new SecureRandom();
		byte[] salt = new byte[4];
		
		// 2. 난수 생성
		r.nextBytes(salt);
		
		// 3. byte to String (10진수의 문자열로 변경)
		StringBuffer sb = new StringBuffer();
		for(byte b : salt) {
			sb.append(String.format("%02x", b));
		}
		
		return sb.toString();
	}
}
