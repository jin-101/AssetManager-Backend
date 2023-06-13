package com.shinhan.assetManager.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.coin.CoinBithumbDTO;
import com.shinhan.assetManager.coin.CoinDtoForReact;
import com.shinhan.assetManager.coin.CoinUpbitDTO;
import com.shinhan.assetManager.jwt.JavaJwt;
import com.shinhan.assetManager.repository.CoinBithumbRepo;
import com.shinhan.assetManager.repository.CoinUpbitRepo;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;

@Service
public class CoinService {

	@Autowired
	JavaJwt jwt;
	@Autowired
	UserRepo uRepo;
	@Autowired
	UserAssetRepo assetRepo;
	@Autowired
	UserAssetDTO userAsset;
	@Autowired
	CoinUpbitRepo upbitRepo;
	@Autowired
	CoinBithumbRepo bitRepo;
	@Autowired
	CoinUpbitDTO upbit;
	@Autowired
	CoinBithumbDTO bit;
	
	public String addUpbit(CoinDtoForReact coin, String token) {
		//UserDTO user = jwt.getUserIdFromJwt(request); // JavaJwt 클래스의 getUserIdFromJwt() 메소드 이용
		UserDTO user = uRepo.findById(token).get();
		
		// 코인 자산 입력 코드
		// (1) 데이터 가공
		String price = coin.getPrice().replace(",", ""); // 금액 , 없애기
		String market = coin.getMarket();
		if(market.equals("업비트")) {
			market = "upbit";
		}else if(market.equals("빗썸")) {
			market = "bithumb";
		}
		String coinName = coin.getCoinName().toUpperCase()+"_"+market; // 대문자로 바꾸기 위한 작업
		// (2) build
		userAsset = UserAssetDTO.builder()
								.assetCode("C2") // C2 : 코인
								.detailCode(coinName) // 세부코드 : (ex) BTC_upbit
								.purchaseDate(coin.getDate())
								.purchasePrice(price) 
								.user(user) 
								.quantity(coin.getQuantity()) 
								.build(); 
		assetRepo.save(userAsset);
		
		String result = null;
		result = "성공";

		return result;
	}

	public Map<String, String> getCoinList(CoinDtoForReact coin) {
		Map<String, String> coinMap = new HashMap<>();

		// ★★★
		// market(업비트 or 빗썸)에 따라 검색되는 코인을 다르게 해주려고 했지만..
		// 그렇게 하려면 axios를 어떻게 써야될지 모르겠어서 일단 바로 데이터를 get할 수 있게 처리하였음.
		// 따라서 key값을 같은 BTC여도 업비트/빗썸 단어를 붙여서 다르게 주었음 (안 그러면 덮어씌워지더라고 key가 동일하니까)
		upbitRepo.findAll().forEach(upbit -> {
			String key = upbit.getCoinName().replace("_upbit", " : 업비트");
			String coinName = upbit.getCoinName().replace("_upbit", "");
			coinMap.put(key, coinName);
		});
		bitRepo.findAll().forEach(bithumb -> {
			String key = bithumb.getCoinName().replace("_bithumb", " : 빗썸");
			String coinName = bithumb.getCoinName().replace("_bithumb", "");
			coinMap.put(key, coinName);
		});
		System.out.println(coinMap);
		
		return coinMap;
	}
	
//	upbitRepo.findAll().forEach(upbit -> {
//		String key = upbit.getCoinName().replace("_upbit", " : 업비트");
//		String coinName = upbit.getCoinName().replace("_upbit", "");
//		coinMap.put(key, coinName);
//	});
//	bitRepo.findAll().forEach(bithumb -> {
//		String key = bithumb.getCoinName().replace("_bithumb", " : 빗썸");
//		String coinName = bithumb.getCoinName().replace("_bithumb", "");
//		coinMap.put(key, coinName);
//	});
//
//	System.out.println(coinMap);
//	return coinMap;

}
