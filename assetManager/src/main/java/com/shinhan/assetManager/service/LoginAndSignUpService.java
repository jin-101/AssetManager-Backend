package com.shinhan.assetManager.service;

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
	
	// 로그인
	public String login(UserDTO userDto) {
		// 아이디 체크 & 비밀번호 체크 2개 메소드를 여기 안에????
		String inputId = userDto.getUserId();
		String inputPw = userDto.getUserPw();
		String result = null;
		
		// 일단 대충 구현
		// (1) id를 통해 pw 찾기
		// (2) 입력한 pw와 DB에 있는 pw가 일치하면 로그인 OK
		UserDTO user = uRepo.findById(inputId).orElse(null); // 여기 ifPresent 쓸지 뭐 쓸지 다시 고민
		if(user == null) {
			result = "넌 못 지나간다";
		}else {
			String userPw = user.getUserPw();
			if(inputPw.equals(userPw)) {
				System.out.println("로그인 성공"); // ★ ㅅㅂ 비번 암호화 해놔서 인식을 못했구나 
				result = "로그인 성공";
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
