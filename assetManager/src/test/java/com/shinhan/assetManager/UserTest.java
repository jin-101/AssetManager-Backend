package com.shinhan.assetManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.EncryptSsn;
import com.shinhan.assetManager.user.UserDTO;

@SpringBootTest
public class UserTest {
	
	@Autowired
	UserRepo uRepo;
	@Autowired
	UserDTO userDto;
	
	//
	@Test
	void checkPw() {
		// 1. 해당 유저의 Id를 통해 salt값을 가져와
		String userId = "djdjdddd";
		String salt = uRepo.findByUserId(userId).getSalt();
		String userPw = uRepo.findByUserId(userId).getUserPw();
		System.out.println(salt);
		
		// 2. 그 salt값과 유저가 입력한 Pw를 이용하여 서로 일치하는지 체크
		String inputPw = "신한1기!!";
		EncryptSsn en = new EncryptSsn();
		String encryptedPw = en.getEncrypt(inputPw, salt);
		if(encryptedPw.equals(userPw)) {
			System.out.println("비번 일치");
			// 비번 일치시엔 그대로 진행시키고
		}else {
			System.out.println("비번 불일치");
			// 비번 불일치시엔 텍스트를 보내서
			// 리액트에서 이 값을 받으면 Alert.alert 뜨게 하면 될 듯
		}
	}
	
	// 회원가입 양식을 통해 => 유저 정보를 DB에 insert하는 작업
	//@Test
	void insertUser() {
		// 1. salt를 생성
		EncryptSsn en = new EncryptSsn();
		String salt = en.getSalt();
		System.out.println("salt: "+salt);
		
		String ssn = "1234561234567";
		String encryptedSsn = en.getEncrypt(ssn, salt);
		
		String pw = "신한1기!";
		String encryptedPw = en.getEncrypt(pw, salt);
		
		// 2. 회원가입 양식을 통해 넘어온 user 데이터를 받아서 (from 리액트)  
		UserDTO user = UserDTO.builder()
				.ssn(encryptedSsn)
				.userId("djdjdddd")
				.userPw(encryptedPw)
				.userEmail("djdjdddd@naver.com")
				.userAddress("동대문구")
				.userName("김용희")
				.salt(salt) 
				.build(); 
		
		System.out.println("user: "+user);
		
		// 3. User 테이블에 save
		uRepo.save(user);
	}

}
