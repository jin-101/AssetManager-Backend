package com.shinhan.assetManager.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;

// 주민번호 암호화를 위한 클래스 (비밀번호는 양방향이라서 ㄴㄴ)
// 단방향 해시 함수 (One-way Hash Function) 사용
// ex. SHA-256
@Component
public class SHA256 {

	public static void main(String[] args) {
		SHA256 en = new SHA256();
		
		String pwd = "내가 암호화 하고 싶은 놈";
		System.out.println("pwd : " + pwd);
		
		// Salt 생성
		String salt = en.getSalt();
		System.out.println("salt : " + salt);
		
		// 최종 pwd 생성
		String res = en.getEncrypt(pwd, salt);
	}
	
	public String getSalt() {
		// 1. Random, byte 객체 생성
		SecureRandom r = new SecureRandom();
		byte[] salt = new byte[3];
		
		// 2. 난수 생성
		r.nextBytes(salt);
		
		// 3. byte to String (10진수의 문자열로 변경)
		StringBuffer sb = new StringBuffer();
		for(byte b : salt) {
			sb.append(String.format("%02x", b));
		}
		
		return sb.toString();
	}
	
	public String getEncrypt(String pwd, String salt) {
		String result = "";
		try {
			// 1. SHA256 알고리즘 객체 생성
			MessageDigest md;
			md = MessageDigest.getInstance("SHA-256");
			
			// 2. pwd와 salt 합친 문자열에 SHA-256 적용
			//System.out.println("pwd + salt 적용 전 : " + pwd+salt);
			md.update((pwd+salt).getBytes());
			byte[] pwdsalt = md.digest();
			
			// 3. byte to String (10진수의 문자열로 변경)
			StringBuffer sb = new StringBuffer();
			for(byte b : pwdsalt) {
				sb.append(String.format("%02x", b));
			}
			
			result = sb.toString();
			//System.out.println("pwd + salt 적용 후 : " + result);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
