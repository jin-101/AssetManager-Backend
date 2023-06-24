package com.shinhan.assetManager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.coin.CoinAssetDTO;
import com.shinhan.assetManager.coin.CoinBithumbDTO;
import com.shinhan.assetManager.coin.CoinDtoForReact;
import com.shinhan.assetManager.coin.CoinUpbitDTO;
import com.shinhan.assetManager.common.DecimalFormatForCurrency;
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
	@Autowired
	DecimalFormatForCurrency dfc;
	
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

	// 코인 검색 기능
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
	
	// 
	public void asdf(List<UserAssetDTO> coinList) {
		
	}
	
	// 자산 탭 - 코인 자산 조회
	public List<CoinAssetDTO> myCoinInfo(String userId) {
		Double totalPurchase;
		CoinAssetDTO coinAsset; 
		UserDTO user = uRepo.findById(userId).get();
		Map<String, CoinAssetDTO> coinMap = new HashMap<>(); // <코인이름, 총매수금액>
		
		List<UserAssetDTO> coinList = assetRepo.findByUserAndAssetCode(user, "C2"); // coinAssetCode == C2
		// (1) 
		for(int i=0; i<coinList.size(); i++) {
			UserAssetDTO userAssetDto = coinList.get(i);
			String detailCode = userAssetDto.getDetailCode(); // BTC_bithumb, BTC_upbit, BTC_upbit
			CoinUpbitDTO upbitCoin = upbitRepo.findById(detailCode).orElse(null);
			CoinBithumbDTO bithumbCoin = bitRepo.findById(detailCode).orElse(null);
			
			coinAsset = new CoinAssetDTO();
			if (upbitCoin != null) {
				// 거래소
				String market = "업비트"; 
				// 현재시세(prev_closing_price) 얻기
				Double currentPrice = Double.parseDouble(upbitCoin.getPrev_closing_price());
				// 매수가 
				Double purchasePrice = Double.parseDouble(userAssetDto.getPurchasePrice());
				// 매수수량
				Double quantity = Double.parseDouble(userAssetDto.getQuantity());
				// 총 매수금액
				totalPurchase = 0.0;
				totalPurchase = purchasePrice * quantity;
				
				// *코인 이름이 같은 놈이 있는지 체크
				CoinAssetDTO presentedCoinAsset = coinMap.get(detailCode);
				if(presentedCoinAsset != null) {
					Double presentedPrice = presentedCoinAsset.getTotalPurchase();
					Double presentedQuantity = presentedCoinAsset.getQuantity();
					if(presentedPrice != 0.0) { // (1) *해당 코인의 값이 이미 있다면
						// DTO 세팅
						totalPurchase += presentedPrice;
						coinAsset.setTotalPurchase(totalPurchase);
						Double totalQuantity = quantity + presentedQuantity;
						coinAsset.setQuantity(totalQuantity);
						coinAsset.setMarket(market);
						coinAsset.setCurrentPrice(currentPrice);
						
						coinMap.put(detailCode, coinAsset);
					}
				}else { // (2) *해당 코인이 처음이라면
					// DTO 세팅
					coinAsset.setMarket(market);
					coinAsset.setCurrentPrice(currentPrice);
					coinAsset.setTotalPurchase(totalPurchase);
					coinAsset.setQuantity(quantity);
					
					coinMap.put(detailCode, coinAsset);
				}
			}else if(bithumbCoin != null) {
				// 거래소
				String market = "빗썸"; 
				// 현재시세(prev_closing_price) 얻기 
				Double currentPrice = Double.parseDouble(bithumbCoin.getPrev_closing_price());
				// 매수가 
				Double purchasePrice = Double.parseDouble(userAssetDto.getPurchasePrice());
				// 매수수량
				Double quantity = Double.parseDouble(userAssetDto.getQuantity());
				// 총 매수금액
				totalPurchase = 0.0;
				totalPurchase = purchasePrice * quantity;
				
				// *코인 이름이 같은 놈이 있는지 체크
				CoinAssetDTO presentedCoinAsset = coinMap.get(detailCode);
				if(presentedCoinAsset != null) {
					Double presentedPrice = presentedCoinAsset.getTotalPurchase();
					Double presentedQuantity = presentedCoinAsset.getQuantity();
					if(presentedPrice != 0.0) { // (1) *해당 코인의 값이 이미 있다면
						// DTO 세팅
						totalPurchase += presentedPrice;
						coinAsset.setTotalPurchase(totalPurchase);
						Double totalQuantity = quantity + presentedQuantity;
						coinAsset.setQuantity(totalQuantity);
						coinAsset.setMarket(market);
						coinAsset.setCurrentPrice(currentPrice);
						
						coinMap.put(detailCode, coinAsset);
					}
				}else { // (2) *해당 코인이 처음이라면
					// DTO 세팅
					coinAsset.setMarket(market);
					coinAsset.setCurrentPrice(currentPrice);
					coinAsset.setTotalPurchase(totalPurchase);
					coinAsset.setQuantity(quantity);
					
					coinMap.put(detailCode, coinAsset);
				}
			}
		}
		System.out.println("왜 콘솔에 안 찍히죵 ?? "+coinMap.toString());
		
		// (2) 코인이름, 평단가, 수익률 (계산 및 setter)
		List<CoinAssetDTO> myCoinList = calcAvgPriceAndReturn(coinMap);
		
		return myCoinList;
	}
	
	// (2) 코인이름, 평단가, 수익률 (계산 및 setter)
	public List<CoinAssetDTO> calcAvgPriceAndReturn(Map<String, CoinAssetDTO> coinMap) {
		List<CoinAssetDTO> myCoinList = new ArrayList<>(); // return할 List
		
		Iterator<Entry<String, CoinAssetDTO>> coinMapIterator = coinMap.entrySet().iterator();
		while(coinMapIterator.hasNext()) {
			Entry<String, CoinAssetDTO> coinEntry = coinMapIterator.next();
			String key = coinEntry.getKey();
			CoinAssetDTO coinDto = coinEntry.getValue();
			
			// 코인이름
			String coinName = key.substring(0, key.indexOf("_"));
			coinDto.setCoinName(coinName);
			
			// 평단가 (avgPrice)
			Double totalPurchase = coinDto.getTotalPurchase();
			Double totalQuantity = coinDto.getQuantity();
			Double avgPrice = totalPurchase / totalQuantity;
			coinDto.setAvgPrice(avgPrice);
			System.out.println("평단가 : " + avgPrice);
			System.out.println("총 수량 : " + totalQuantity);
			
			// 수익률
			Double currentPrice = coinDto.getCurrentPrice();
			String rateOfReturn = dfc.percent((currentPrice - avgPrice) / avgPrice);
			coinDto.setRateOfReturn(rateOfReturn);
			
			myCoinList.add(coinDto);
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
