package com.shinhan.assetManager.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.coin.CoinBithumbDTO;
import com.shinhan.assetManager.coin.CoinUpbitDTO;
import com.shinhan.assetManager.deposit.DepositSavingsDTO;
import com.shinhan.assetManager.repository.AptRecentTradeRepo;
import com.shinhan.assetManager.repository.CoinBithumbRepo;
import com.shinhan.assetManager.repository.CoinUpbitRepo;
import com.shinhan.assetManager.repository.DepositDTORepo;
import com.shinhan.assetManager.repository.HouseholdAccountsRepository;
import com.shinhan.assetManager.repository.StockRepo;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserLiabilityRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.AES256;
import com.shinhan.assetManager.user.SHA256;
import com.shinhan.assetManager.user.Salt;
import com.shinhan.assetManager.user.UserAssetDTO;
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
	@Autowired
	LoginAndSignUpService lasService;
	@Autowired
	StockService stockService;
	@Autowired
	UserAssetRepo uaRepo; // 자산
	@Autowired
	UserLiabilityRepo ulRepo; // 부채 
	@Autowired
	AptRecentTradeRepo artRepo; // E1 : 부동산
	@Autowired
	CoinUpbitRepo upbitRepo; // C2 : 코인
	@Autowired
	CoinBithumbRepo bithumbRepo; // C2 : 코인
	@Autowired
	StockRepo sRepo; // C1 : 주식
	@Autowired
	HouseholdAccountsRepository haRepo; // A1 : 가계부잔액
	@Autowired
	DepositDTORepo depositDtoRepo;
	@Autowired
	GoldService goldService;
	@Autowired
	CurrencyService currencyService;
	
	String exchangeAssetCode = "A2";
	String depositAssetCode = "B1";
	String savingsAssetCode = "B2";
	String stockAssetCode = "C1";
	String coinAssetCode = "C2";
	String aptAssetCode = "E1";
	String carAssetCode = "E2";
	String goldAssetCode = "E3";

	// 이메일 체크
	public String checkEmail(UserDTO userDto) {
		String userEmail = userDto.getUserEmail();
		System.out.println("이메일 인증 요청 들어옴");
		return "hi";
	}

	// 로그인 
	public Map<String,String> login(UserDTO userDto) {
		String inputId = userDto.getUserId();
		String inputPw = userDto.getUserPw();
		String result = null; // ★ 3가지 경우에 따라 Front에 출력되는 result를 다르게
		Long leftTime = 30L; // 로그인 잠금 남은 시간
		Long currentLoginDate = new Date().getTime() / 1000; // 현재 로그인 시점의 시간
		Map<String,String> loginInfo = new HashMap<>();
		loginInfo.put("userName", "");
		// 1. 로그인 공백 체크
		if (inputId.equals("")) {
			result = "아이디를 입력해주세요";
			loginInfo.put("result", result);
			return loginInfo;
		} else if (inputPw.equals("")) {
			result = "비밀번호를 입력해주세요";
			loginInfo.put("result", result);
			return loginInfo;
		}

		// 2. 로그인 체크
		UserDTO user = uRepo.findById(inputId).orElse(null); // return값 => null 또는 UserDTO 1개
		if (user == null) { // (i) 아이디가 없는 경우
			result = "아이디가 존재하지 않습니다";
			loginInfo.put("result", result);
		} else {
			// 3. 로그인 잠금 체크 (by 로그인 횟수 제한 초과로 인한)
			String accountLockStatus = user.getAccountLockStatus();
			Long latestLoginDate = user.getLatestLoginDate();
			// (1) 30초 이미 지난 경우
			if (latestLoginDate != null && (currentLoginDate - latestLoginDate) > 30) { // ★ (1)예전에 로그인했던 시간이 DB에 저장돼있고,
																						// (2)이미 30초가 지난 상태라면
				user.setAccountLockStatus("N");
				user.setLoginFailCount(0);
				user.setLatestLoginDate(currentLoginDate); // 로그인 시각도 현재 시각으로 리셋
				uRepo.save(user);
			}
			// (2) 30초 내에 로그인 재시도 하는 경우
			if (accountLockStatus.equals("Y")) {
				leftTime = 30 - (currentLoginDate - latestLoginDate);
				if (leftTime >= 0) {
					result = "아직 로그인을 할 수 없습니다. 남은시간 (" + leftTime + "초)";
					loginInfo.put("result", result);
					return loginInfo;
				} else {
					user.setAccountLockStatus("N"); // "N" : 30초 지나면 잠금해제
					user.setLoginFailCount(0); // 0 : 로그인 실패횟수도 초기화
					uRepo.save(user);
				}
			}

			// 2. 로그인 체크 中 비밀번호 체크
			String userId = user.getUserId();
			String encryptedPw = user.getUserPw();
			String salt = user.getSalt();
			String text = inputPw + salt;
			try {
				String encryptedText = aes256.encryptAES256(text);
				if (encryptedText.equals(encryptedPw)) { // (ii) 비밀번호 맞은 경우
					// ★ 로그인 성공시 JWT 토큰 생성해서 리액트로 보냄

					//JavaJwt jwt = new JavaJwt();
					//String token = jwt.createToken(userId); 
					result = "로그인성공";
					loginInfo.put("result", result);
					loginInfo.put("userName", user.getUserName());
					//session.setAttribute(userId, user);

				} else { // (iii) 비밀번호 틀린 경우
					int loginFailCount = user.getLoginFailCount();
					result = "비밀번호가 틀렸습니다." + " 남은횟수 (" + (5 - loginFailCount) + "회)";
					loginInfo.put("result", result);
					// 3. 로그인 잠금 체크 - 로그인 시도 횟수 제한
					loginFailCount++;
					System.out.println("현재 로그인 시도 횟수 : " + loginFailCount);
					user.setLoginFailCount(loginFailCount);
					uRepo.save(user);
					if (loginFailCount > 5) {
						result = "비밀번호 5회 연속 실패로 인하여 30초 동안 잠금되었습니다";
						loginInfo.put("result", result);
						user.setAccountLockStatus("Y");
						if (loginFailCount == 6) {
							latestLoginDate = new Date().getTime() / 1000;
							user.setLatestLoginDate(latestLoginDate);
						}
						currentLoginDate = new Date().getTime() / 1000;
						user.setLatestLoginDate(currentLoginDate);
						uRepo.save(user);
					}
				}
			} catch (Exception e) {
				System.out.println("LoginAndSignUpService : login 메소드에서 에러");
				e.printStackTrace();
			}
		}
		return loginInfo;
	}

	// 로그아웃
	public String logout(UserDTO userDto) {
		
		return "로그아웃 성공";
	}

	// 회원가입
	public String signUp(UserDTO userDto) {
		// (0) 최대 회원가입 계정 수 체크
		String result = checkMaxAccount(userDto);

		if (result == null) {
			// 1. 주민번호 암호화 (단방향 by SHA256) => (정정. 양방향 by AES256)
			// (1) salt 생성
			String salt = Salt.getSalt();
			// (2) 암호화
			String ssn = userDto.getSsn(); // 암호화하고 싶은 주민번호
			String pw = userDto.getUserPw();
			String text = ssn + salt;
			String encryptedSsn = null;
			try {
				encryptedSsn = aes256.encryptAES256(text);
				// 2. 비밀번호 암호화 (양방향 by AES256)
				String text2 = pw + salt;
				String encryptedPw = aes256.encryptAES256(text2);

				// 3. 암호화된 ssn, pw + "N" + salt
				userDto.setSsn(encryptedSsn);
				userDto.setUserPw(encryptedPw);
				userDto.setAccountLockStatus("N");
				userDto.setSalt(salt);

				// 마지막. User 테이블에 save
				uRepo.save(userDto);
				result = "회원가입이 완료되었습니다";
			} catch (Exception e) {
				System.out.println("LoginAndSignUpService : 회원가입에서 에러");
				e.printStackTrace();
			}
		}
		return result;
	}

	// 회원가입 中 아이디 중복 체크
	public String checkDuplicatedId(String userId) {
		String result = null;
		// String inputId = userDto.getUserId(); // (1) 앱에서 중복체크 버튼을 누르면 => Id를 받아서
		UserDTO user = uRepo.findById(userId).orElse(null); // (2) 그 Id가 있는지 체크
		// ★ findByID()의 리턴값이 Optional<>이므로 orElse()나 ifPresent()
		// 등을 이용하여 자체적으로 null값 체크가 가능! (굳이 null 체크 로직 안 짜도 됨 귀찮게)
		if (user == null) {
			result = "사용가능한 ID입니다";
		} else {
			result = "중복된 아이디가 존재합니다";
		}
		return result;
	}
	
	// ★ 아이디 찾기
	public List<String> findUserId(UserDTO userDto) {
		List<String> idList = new ArrayList<>();
		// (1) 유저 정보를 받음 (이름, 폰번 2개 필요) (★ 원래 주민번호로 하려 했더니 salt값이 다 달라서 이걸 어케할지가 좀 고민..)
		String inputUserName = userDto.getUserName();
		String inputPhoneNumber = userDto.getPhoneNumber();

		// (2) 2개 정보에 해당하는 Id가 있는지 먼저 체크 (이것도 이름이나 주민번호 잘못 입력했는지 체크해주는게 좋을라나..?)
		List<UserDTO> checkUserList = new ArrayList<>();
		List<UserDTO> userList = new ArrayList<>();
		checkUserList = uRepo.findByUserNameAndPhoneNumber(inputUserName, inputPhoneNumber);
		System.out.println(checkUserList);

		if (checkUserList.size() == 0) {
			idList.add("존재하는 ID가 없습니다"); // ID 잘못 입력시 '해당문구'를 리턴
		}

		checkUserList.forEach(user -> {
			userList.add(user);
		});
		userList.forEach(user -> {
			String userId = user.getUserId();
			// 리액트에는 List나 Map 형태로 보내면 될 듯? (근데 그럼 리턴 타입이 서로 달라서 흠..)
			idList.add(userId);
		});
		return idList;
	} 

	// ★ 비밀번호 찾기
	public String findUserPw(UserDTO userDto) {
		String result = null;
		// (1) 유저 정보를 받음 (주민번호, Id 2개 필요)
		String inputSsn = userDto.getSsn();
		String inputId = userDto.getUserId();

		// (2) Id로부터 salt 얻고 => (i)ID 올바르게 입력했는지 체크 / (ii)주민번호 틀렸다고 알려주기 / (iii)주민번호 일치시
		// 패스워드 알려주기
		// (i)
		UserDTO user = uRepo.findById(inputId).orElse(null);
		if (user == null) {
			result = "존재하지 않는 ID입니다";
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
				result = realPw; // 기존 비밀번호 return
			} else { // 틀리면 주민번호 틀렸다고 알려주기
				result = "error";
			}
		} catch (Exception e) {
			System.out.println("LoginAndSignUpService : 비밀번호 찾기에서 에러");
			e.printStackTrace();
		}
		return result;
	}
	
	// 새 비밀번호 등록 (비밀번호 찾기 page)
	public String registerUserPw(UserDTO userDto) {
		String result = null;
		String inputUserId = userDto.getUserId(); // 등록 전에 살짝 바꾸는 걸 어떻게 방지하지??
		String newUserPw = userDto.getUserPw();
		
		// pw 암호화한 후 회원정보 업데이트
		UserDTO user = uRepo.findById(inputUserId).get();
		String salt = user.getSalt(); // 기존 salt값 뽑아내기
		String text = newUserPw+salt;
		String encryptedPw = null;
		try {
			encryptedPw = aes256.encryptAES256(text);
		} catch (Exception e) {
			System.out.println("LoginAndSignUpService : 새 비밀번호 등록에서 에러");
			e.printStackTrace();
		}
		user.setUserPw(encryptedPw);
		uRepo.save(user);
		result = "다시 로그인해주세요";
		
		return result;
	}

	// 1인당 최대 3개의 아이디만 가질 수 있게 제한 (= 회원가입 가능 여부 체크버튼)
	public String checkMaxAccount(UserDTO userDto) { // ★ 아예 통 메소드로 만들어서 사용하면 좋을 듯
		// (1) 회원가입 양식에 입력한 이름과 주민번호를 파라미터로 받아서
		String result = null;
		String inputUserName = userDto.getUserName();
		String inputSsn = userDto.getSsn();

		// (2) 이름으로 salt를 각각 찾아서 => ssn 암호화하고 => 암호화된 ssn의 형태를 비교
		List<String> idList = new ArrayList<>();
		uRepo.findByUserName(inputUserName).forEach(user -> {
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
			result = "최대 3개의 계정만 가질 수 있습니다. 기존 ID를 삭제해주세요";
		}
		return result;
	}
	
	// 총자산 얻기
	public void getTotalAsset(String userId) {
		// (1) 총 주식 자산
		Long totalStockAsset = lasService.getTotalStock(userId);
		
		// (2) 총 코인 자산
		getTotalCoin(userId);
		
		getTotalDepositAndSavings(userId);
	}
	
	// (1) 총 주식 자산
	public Long getTotalStock(String userId) {
		String response = stockService.showHoldingStocks(userId); // userId
		System.out.println(response);
		
		JSONArray jsonArr = new JSONArray(response);
		System.out.println(jsonArr);
		
		Long total = 0L;
		Long eachStockAsset = 0L;
		for(int i=0; i<jsonArr.length(); i++) {
			JSONObject jsonObj = (JSONObject) jsonArr.get(i);
			Integer stockPrice = (Integer) jsonObj.get("stockPrice");
			Integer totalShares = (Integer) jsonObj.get("totalShares");
			eachStockAsset = (long) (stockPrice * totalShares);
			total += eachStockAsset;
		}
		System.out.println("총 주식 자산 : " + total);
		
		return total;
	}
	
	// (2) 총 코인 자산
	public void getTotalCoin(String userId) {
		UserDTO user = uRepo.findById(userId).get();
		List<UserAssetDTO> coinList = uaRepo.findByUserAndAssetCode(user, coinAssetCode); // coinAssetCode == C2
		
		System.out.println("총 코인 List 수 : " + coinList.size());
		
		Double totalCoinAsset = 0.0;
		Double eachUpbitCoin = 0.0;
		Double eachBithumbCoin = 0.0;
		for(int i=0; i<coinList.size(); i++) {
			// 수량(quantity) 얻기
			UserAssetDTO coinAssetDto = coinList.get(i);
			Double quantity = Double.parseDouble(coinAssetDto.getQuantity());
			
			// 현재시세(prev_closing_price) 얻기 - 1)업비트, 2)빗썸
			String detailCode = coinAssetDto.getDetailCode();
			CoinUpbitDTO upbitCoin = upbitRepo.findById(detailCode).orElse(null);
			if(upbitCoin != null) { // ★ 가끔 코인이 상장폐지가 되는 경우가 있는데, null 처리 안해주면 나중에 에러날 수도
				String price = upbitCoin.getPrev_closing_price();
				Double prev_closing_price = Double.parseDouble(price);
				eachUpbitCoin = prev_closing_price * quantity;
				totalCoinAsset += eachUpbitCoin;
			}
			CoinBithumbDTO bithumbCoin = bithumbRepo.findById(detailCode).orElse(null);
			if(bithumbCoin != null) { // ★ 가끔 코인이 상장폐지가 되는 경우가 있는데, null 처리 안해주면 나중에 에러날 수도
				String price = bithumbCoin.getPrev_closing_price();
				Double prev_closing_price = Double.parseDouble(price);
				eachBithumbCoin = prev_closing_price * quantity;
				totalCoinAsset += eachBithumbCoin;
			}
		}
		System.out.println("총 코인 자산 : " + totalCoinAsset);
	}
	
	// (3) 총 예적금 자산
	public void getTotalDepositAndSavings(String userId) {
		UserDTO user = uRepo.findById(userId).get();
		List<UserAssetDTO> depositAndSavingsList = uaRepo.findByUserAndAssetCodeStartingWith(user, "B"); // B1, B2
		
		System.out.println("총 예적금 List 수 : " + depositAndSavingsList.size());
		
		Long total = 0L;
		for(int i=0; i<depositAndSavingsList.size(); i++) {
			UserAssetDTO depositAndSavingsDto = depositAndSavingsList.get(i);
			Long detailCode = Long.parseLong(depositAndSavingsDto.getDetailCode());
			DepositSavingsDTO depositSavingsDto = depositDtoRepo.findByDetailCode(detailCode);
			Integer price = Integer.parseInt(depositSavingsDto.getPrice());
			total += price;
		}
		System.out.println("총 예적금 자산 : " + total);
	}
	
	// (4) 총 부동산 자산
	public void getTotalApt() {
		
	}
	
	// (5) 총 금, 외환 자산
	public void getTotalGoldAndExchange() {
		
	}
	
	// (6) 총 자동차 자산
	public void getTotalCar() {
		
	}

}