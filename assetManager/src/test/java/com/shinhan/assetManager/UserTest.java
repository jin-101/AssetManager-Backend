package com.shinhan.assetManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.AES256;
import com.shinhan.assetManager.user.SHA256;
import com.shinhan.assetManager.user.UserDTO;

@SpringBootTest
public class UserTest {
	
	@Autowired
	UserRepo uRepo;
	@Autowired
	UserDTO userDto;
	@Autowired
	SHA256 sha256; // 단방향
	@Autowired
	AES256 aes256; // 양방향
	
	// 비밀번호 찾기 기능
	//@Test
	void findPw() throws Exception {
		// (1) 유저 정보를 받음 (주민번호, Id)
		String inputSsn = "1234561234567";
		String inputId = "jin";
		
		// (2) Id로부터 salt 얻기
		String salt = uRepo.findByUserId(inputId).getSalt();
		String ssn = uRepo.findByUserId(inputId).getSsn();
		String pw = uRepo.findByUserId(inputId).getUserPw();
		System.out.println(salt);
		
		// (3) 
		String encryptedSsn = sha256.getEncrypt(inputSsn, salt);
		
		if(encryptedSsn.equals(ssn)) {
			System.out.println("주민번호 일치");
			String decryptedPw = aes256.decryptAES256(pw);
			String realPw = decryptedPw.replace(salt, "");
			System.out.println(realPw);
		}else {
			System.out.println("주민번호 불일치");
			// 틀리게 입력했으므로 제대로 입력하라고 띄워주기
		}
		
	}
	
	// 로그인시 비밀번호 체크 로직 (암호화된 비밀번호 vs 유저가 입력한 비밀번호)
	//@Test
	void checkPw() throws Exception {
		// 1. 해당 유저의 Id를 통해 salt값을 가져와
		String inputId = "jin";
		// ★ 아이디가 틀렸을 경우에도 Alert 표시해줘야 함!!
		String salt = uRepo.findByUserId(inputId).getSalt();
		String userPw = uRepo.findByUserId(inputId).getUserPw();
		System.out.println(salt);
		
		// 2. salt + 유저가 입력한 Pw => 복호화
		String inputPw = "1234";
		String text = inputPw+salt;
		String encryptedPw = aes256.encryptAES256(text);
		
		// 3. 비교
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
	void insertUser() throws Exception {
		// (0) 주민번호 암호화 (단방향 by SHA256)
		// (1) salt 생성
		String salt = sha256.getSalt();
		System.out.println("salt: "+salt);
		
		// (2) 회원가입 양식을 통해 넘어온 user 데이터를 받아서 (from 리액트)
		
		// (3) 암호화
		String ssn = "1234561234567"; // 암호화하고 싶은 주민번호
		String encryptedSsn = sha256.getEncrypt(ssn, salt);
		
		// (4) 비밀번호 암호화 (양방향 by AES256)
		String pw = "1234";
		String text = pw+salt;
		String encryptedPw = aes256.encryptAES256(text);
		
		// 예시
		UserDTO user = UserDTO.builder()
					.ssn(encryptedSsn)
					.userId("jin")
					.userEmail("djdjdddd@naver.com")
					.userAddress("구리시")
					.userName("한진")
					.userPw(encryptedPw)
					.salt(salt) 
					.build(); 
		System.out.println("user: "+user);
		
		// 최종. User 테이블에 save
		uRepo.save(user);
	}

}
