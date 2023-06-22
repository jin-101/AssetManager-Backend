package com.shinhan.assetManager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shinhan.assetManager.coin.CoinAssetDTO;
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
public class CoinService implements AssetService {

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
	
	// 자산 탭 - 코인 자산 조회
	public List<CoinAssetDTO> myCoinInfo(String userId) {
		List<CoinAssetDTO> myCoinList = new ArrayList<>();
		UserDTO user = uRepo.findById(userId).get();
		List<UserAssetDTO> coinList = assetRepo.findByUserAndAssetCode(user, "C2"); // coinAssetCode == C2
		for(int i=0; i<coinList.size(); i++) {
			String detailCode = coinList.get(i).getDetailCode();
			CoinUpbitDTO upbitCoin = upbitRepo.findById(detailCode).orElse(null);
			if (upbitCoin != null) {
				// 거래소
				String market = "업비트";
				// 현재시세(prev_closing_price) 얻기 - 1)업비트, 2)빗썸
				Double currentPrice = Double.parseDouble(upbitCoin.getPrev_closing_price());
				// 매수가 
//				Double purchasePrice = 
				// 수익률
				
			}
		}
		
		Double total = 0.0;
		for (int i = 0; i < coinList.size(); i++) {
			UserAssetDTO coinAssetDto = coinList.get(i);
			String detailCode = coinAssetDto.getDetailCode();
			CoinUpbitDTO upbitCoin = upbitRepo.findById(detailCode).orElse(null);
			if (upbitCoin != null) {
				// 거래소
				String market = "업비트";
				// 현재시세(prev_closing_price) 얻기 - 1)업비트, 2)빗썸
				Double currentPrice = Double.parseDouble(upbitCoin.getPrev_closing_price());
				// 매수가 
//				Double purchasePrice = 
				// 수익률
				
			}
//			CoinBithumbDTO bithumbCoin = bitRepo.findById(detailCode).orElse(null);
//			if (bithumbCoin != null) { // ★ 가끔 코인이 상장폐지가 되는 경우가 있는데, null 처리 안해주면 나중에 에러날 수도
//				// 거래소
//				String market = "빗썸";
//				// 현재시세(prev_closing_price) 얻기 - 1)업비트, 2)빗썸
//				Double currentPrice = Double.parseDouble(bithumbCoin.getPrev_closing_price());
//			}
		}
		
		return myCoinList;
	}

	@Override
	public String getPrice(String assetCode, String detailCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReturn() {
		// TODO Auto-generated method stub
		return null;
	}

}
