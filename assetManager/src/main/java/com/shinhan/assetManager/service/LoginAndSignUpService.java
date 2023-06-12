package com.shinhan.assetManager.service;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.AES256;
import com.shinhan.assetManager.user.SHA256;
import com.shinhan.assetManager.user.Salt;
import com.shinhan.assetManager.user.UserDTO;

@Service
public class LoginAndSignUpService {

	@Autowired
	UserRepo uRepo;
	@Autowired
	UserDTO userDto;
	@Autowired
	SHA256 sha256; // 단방향
	@Autowired
	AES256 aes256; // 양방향
	
	// 이메일 체크
	public String checkEmail(UserDTO userDto) {
		String userEmail = userDto.getUserEmail();
		System.out.println("이메일 인증 요청 들어옴");
		return "hi";
	}
	
	// 로그인
	public String login(UserDTO userDto) {
		String inputId = userDto.getUserId();
		String inputPw = userDto.getUserPw();
		String result = null; // ★ 3가지 경우에 따라 Front에 출력되는 result를 다르게
		Long leftTime = 30L; // 로그인 잠금 남은 시간
		Long currentLoginDate = new Date().getTime()/1000; // 현재 로그인 시점의 시간
		
		// 1. 로그인 공백 체크
		if(inputId.equals("")) {
			result = "아이디를 입력해주세요";
			return result;
		}else if(inputPw.equals("")) {
			result = "비밀번호를 입력해주세요";
			return result;
		}
		
		// 2. 로그인 체크
		UserDTO user = uRepo.findById(inputId).orElse(null); // return값 => null 또는 UserDTO 1개
		if(user == null) { // (i) 아이디가 없는 경우
			result = "아이디가 존재하지 않습니다"; 
		}else { 
			// 3. 로그인 잠금 체크 (by 로그인 횟수 제한 초과로 인한)
			String accountLockStatus = user.getAccountLockStatus();
			Long latestLoginDate = user.getLatestLoginDate();
			// (1) 30초 이미 지난 경우
			if(latestLoginDate != null && (currentLoginDate - latestLoginDate) > 30) { // ★ (1)예전에 로그인했던 시간이 DB에 저장돼있고, (2)이미 30초가 지난 상태라면
				user.setAccountLockStatus("N"); 
				user.setLoginFailCount(0); 
				user.setLatestLoginDate(currentLoginDate); // 로그인 시각도 현재 시각으로 리셋
				uRepo.save(user);
			}
			// (2) 30초 내에 로그인 재시도 하는 경우
			if(accountLockStatus.equals("Y")) {
				leftTime = 30 - (currentLoginDate - latestLoginDate);
				if(leftTime >= 0) {
					result = "아직 로그인을 할 수 없습니다. 남은시간 ("+leftTime+"초)";
					return result;
				}else {
					user.setAccountLockStatus("N"); // "N" : 30초 지나면 잠금해제
					user.setLoginFailCount(0); // 0 : 로그인 실패횟수도 초기화
					uRepo.save(user);
				}
			}
			
			// 2. 로그인 체크 中 비밀번호 체크
			String encryptedPw = user.getUserPw();
			String salt = user.getSalt();
			String text = inputPw+salt;
			try {
				String encryptedText = aes256.encryptAES256(text);
				if(encryptedText.equals(encryptedPw)) { // (ii) 비밀번호 맞은 경우
					result = "로그인 성공";
				}else { // (iii) 비밀번호 틀린 경우
					int loginFailCount =  user.getLoginFailCount();
					result = "비밀번호가 틀렸습니다." + " 남은횟수 ("+(5-loginFailCount)+"회)";
					
					// 3. 로그인 잠금 체크 - 로그인 시도 횟수 제한
					loginFailCount++;
					System.out.println("현재 로그인 시도 횟수 : "+loginFailCount);
					user.setLoginFailCount(loginFailCount);
					uRepo.save(user);
					if(loginFailCount > 5) {
						result = "비밀번호 5회 연속 실패로 인하여 30초 동안 잠금되었습니다";
						user.setAccountLockStatus("Y");
						if(loginFailCount == 6) {
							latestLoginDate = new Date().getTime()/1000;
							user.setLatestLoginDate(latestLoginDate);
						}
						currentLoginDate = new Date().getTime()/1000;
						user.setLatestLoginDate(currentLoginDate);
						uRepo.save(user);
					}
				}
			} catch (Exception e) {
				System.out.println("LoginAndSignUpService : login 메소드에서 에러");
				e.printStackTrace();
			}
		}
		return result;
	}

	// 회원가입 
	// (양식을 통해 => 유저 정보를 DB에 insert하는 작업)
	public void signUp(UserDTO userDto){
		// (0) 주민번호 암호화 (단방향 by SHA256) => (정정. 양방향 by AES256)
		// (1) salt 생성
		String salt = Salt.getSalt();
		System.out.println("salt: " + salt);

		// (2) 회원가입 양식을 통해 넘어온 user 데이터를 받아서 (from 리액트)

		// (3) 암호화
		String ssn = "987654-1234567"; // 암호화하고 싶은 주민번호
		String text = ssn + salt;
		String encryptedSsn;
		try {
			encryptedSsn = aes256.encryptAES256(text);
			// (4) 비밀번호 암호화 (양방향 by AES256)
			String pw = "1234";
			String text2 = pw + salt;
			String encryptedPw = aes256.encryptAES256(text2);

			// 예시
			UserDTO user = UserDTO.builder().ssn(encryptedSsn).userPw(encryptedPw).salt(salt).userId("anotherJin")
					.userEmail("anotherJin@naver.com").phoneNumber("01098765432").userName("한진").build();
			System.out.println("user: " + user);

			// 최종. User 테이블에 save
			uRepo.save(user);
		} catch (Exception e) {
			System.out.println("LoginAndSignUpService : 회원가입에서 에러");
			e.printStackTrace();
		}
	}

	// 비밀번호 체크 
	public void checkPw(UserDTO userDto){
		String inputId = "jin";
		String inputPw = "12345";
		// 1. 해당 유저의 Id를 통해 salt값을 가져와
		// ★ 아이디가 틀렸을 경우에도 Alert 표시해줘야 함!!
		String salt = uRepo.findById(inputId).orElse(null).getSalt();

		// 2. salt + DB에 있는 Pw => 복호화
		String PwInDB = uRepo.findById(inputId).orElse(null).getUserPw();
		String encryptedPw;
		try {
			encryptedPw = aes256.decryptAES256(PwInDB);
			// 대조군
			String text = inputPw + salt; // 비교군

			// 3. 비교
			if (text.equals(encryptedPw)) {
				System.out.println("비번 일치");
				// 비번 일치시엔 그대로 진행시키고
			} else {
				System.out.println("비번 불일치");
				// 비번 불일치시엔 텍스트를 보내서
				// 리액트에서 이 값을 받으면 Alert.alert 뜨게 하면 될 듯
			}
		} catch (Exception e) {
			System.out.println("LoginAndSignUpService : 비밀번호 체크에서 에러");
			e.printStackTrace();
		} 
	}

	// ★ 비밀번호 찾기
	public void findUserPw(UserDTO userDto) {
		// (1) 유저 정보를 받음 (주민번호, Id 2개 필요)
		String inputSsn = "123456-1234567";
		String inputId = "jin";

		// (2) Id로부터 salt 얻고 => (i)ID 올바르게 입력했는지 체크 / (ii)주민번호 틀렸다고 알려주기 / (iii)주민번호 일치시
		// 패스워드 알려주기
		// (i)
		UserDTO user = uRepo.findById(inputId).orElse(null);
		if (user == null) {
			System.out.println("존재하지 않는 ID입니다");
			return;
		}
		String salt = user.getSalt();

		// (ii)
		String ssn = uRepo.findById(inputId).orElse(null).getSsn();
		String decryptedSsn;
		try {
			decryptedSsn = aes256.decryptAES256(ssn);
			String text = inputSsn + salt;
			if (text.equals(decryptedSsn)) { // 주민번호 비교 체크 => 일치하면 (iii) 진행
				// (iii)
				String pw = uRepo.findById(inputId).orElse(null).getUserPw();
				String decryptedPw = aes256.decryptAES256(pw);
				String realPw = decryptedPw.replace(salt, "");
				System.out.println("찾으신 회원님의 비밀번호는 : " + realPw + " 입니다");
			} else { // 틀리면 주민번호 틀렸다고 알려주기
				System.out.println("주민번호가 일치하지 않습니다");
			}
		} catch (Exception e) {
			System.out.println("LoginAndSignUpService : 비밀번호 찾기에서 에러");
			e.printStackTrace();
		}
	}

	// ★ 아이디 찾기
	public void findUserId(UserDTO userDto) {
		// (1) 유저 정보를 받음 (이름, 폰번 2개 필요) (★ 원래 주민번호로 하려 했더니 salt값이 다 달라서 이걸 어케할지가 좀 고민..)
		String inputUserName = "한진";
		String inputPhoneNumber = "01012345678";

		// (2) 2개 정보에 해당하는 Id가 있는지 먼저 체크 (이것도 이름이나 주민번호 잘못 입력했는지 체크해주는게 좋을라나..?)
		List<UserDTO> checkUserList = new ArrayList<>();
		List<UserDTO> userList = new ArrayList<>();
		checkUserList = uRepo.findByUserNameAndPhoneNumber(inputUserName, inputPhoneNumber);

		if (checkUserList.size() == 0) {
			System.out.println("존재하는 ID가 없습니다");
			return;
		}

		checkUserList.forEach(user -> {
			userList.add(user);
		});
		userList.forEach(user -> {
			System.out.println("가입하신 ID : " + user.getUserId());
			// 리액트에는 List나 Map 형태로 보내면 될 듯? (근데 그럼 리턴 타입이 서로 달라서 흠..)
		});
	}

	// 아이디 중복 체크
	public void checkDuplicatedId(UserDTO userDto) {
		// (1) 앱에서 중복체크 버튼을 누르면 => Id를 받아서
		String inputId = "jin";
		// (2) 그 Id가 있는지 체크
		UserDTO user = uRepo.findById(inputId).orElse(null); // ★ findByID()의 리턴값이 Optional<>이므로 orElse()나 ifPresent()
																// 등을 이용하여 자체적으로 null값 체크가 가능! (굳이 null 체크 로직 안 짜도 됨
																// 귀찮게)
		if (user == null) {
			System.out.println("사용가능한 ID입니다");
		} else {
			System.out.println("중복된 아이디가 존재합니다");
		}
		System.out.println(user);

	}

	// 1인당 최대 3개의 아이디만 가질 수 있게 제한 (= 회원가입 가능 여부 체크버튼)
	public void checkMaxAccount(UserDTO userDto) { // ★ 아예 통 메소드로 만들어서 사용하면 좋을 듯
		// (1) 회원가입 양식에 입력한 이름과 주민번호를 파라미터로 받아서
		String inputName = "한진";
		String inputSsn = "987654-1234567";

		// (2) 이름으로 salt를 각각 찾아서 => ssn 암호화하고 => 암호화된 ssn의 형태를 비교
		List<String> idList = new ArrayList<>();
		uRepo.findByUserName(inputName).forEach(user -> {
			// (i) 즉, forEach로 하나하나 돌려가며
			String salt = user.getSalt();
			String ssn = user.getSsn();
			String text = inputSsn + salt;
			try {
				if (aes256.encryptAES256(text).equals(ssn)) { // (ii) 암호화된 ssn을 서로 비교하여 동일하다면!
					idList.add(user.getUserId()); // (iii) 회원의 id를 List에 저장 (else면 그냥 아무것도 X)
				}
			} catch (Exception e) {
				System.out.println("LoginAndSignUpService : 1인당 최대 3개에서 에러");
				e.printStackTrace();
			}
		});
		System.out.println("찾으시는 ID : " + idList);

		if (idList.size() > 3) { // (iv) 현재 보유한 Id가 3개 초과라면
			System.out.println("최대 3개의 계정만 가질 수 있습니다. 라고 고지해주기");
		} else {
			System.out.println("회원가입이 가능합니다");
		}
	}
}