package com.shinhan.assetManager.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.coin.CoinBithumbDTO;
import com.shinhan.assetManager.coin.CoinUpbitDTO;
import com.shinhan.assetManager.common.DecimalFormatForCurrency;
import com.shinhan.assetManager.deposit.DepositSavingsDTO;
import com.shinhan.assetManager.dto.HouseholdAccountsDTO;
import com.shinhan.assetManager.repository.AptRecentTradeRepo;
import com.shinhan.assetManager.repository.CarDTOrepo;
import com.shinhan.assetManager.repository.CoinBithumbRepo;
import com.shinhan.assetManager.repository.CoinUpbitRepo;
import com.shinhan.assetManager.repository.DepositDTORepo;
import com.shinhan.assetManager.repository.HouseholdAccountsRepository;
import com.shinhan.assetManager.repository.StockRepo;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserLiabilityRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;
import com.shinhan.assetManager.user.UserLiabilityDTO;

@Service
public class TotalService {
	
	@Autowired
	UserRepo uRepo;
	@Autowired
	UserDTO userDto;
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
	CarDTOrepo carDtoRepo;
	@Autowired
	GoldService goldService;
	@Autowired
	CurrencyService currencyService;
	@Autowired
	DecimalFormatForCurrency dfc;
	
	String exchangeAssetCode = "A2";
	String depositAssetCode = "B1";
	String savingsAssetCode = "B2";
	String stockAssetCode = "C1";
	String coinAssetCode = "C2";
	String aptAssetCode = "E1";
	String carAssetCode = "E2";
	String goldAssetCode = "E3";
	
	// 총자산 얻기 - String
	public Double getTotalAsset(String userId) {
		Double total = 0.0;
		
		// (1) 총 주식 자산
		Long totalStock = getTotalStock(userId);
		// (2) 총 코인 자산
		Double totalCoin = getTotalCoin(userId);
		// (3) 총 예적금
		Long totalDepositAndSavings = getTotalDepositAndSavings(userId);
		// (4) 총 부동산
		Long totalApt = getTotalApt(userId);
		// (5) 총 금, 외환
		Double totalGoldAndExchange = getTotalGoldAndExchange(userId);
		// (6) 총 자동차
		Long totalCar = getTotalCar(userId);
		// (7) 총 가계부잔액
		Integer totalAccountBalance = getTotalAccountBalance(userId);
		
		total = totalStock+totalCoin+totalDepositAndSavings+totalApt+totalGoldAndExchange+totalCar+totalAccountBalance;
		
		return total; 
	}
	
	// 총자산, 총부채 얻기 - Map
	public Map<String, Object> getTotalAssetMap(String userId) {
		Map<String, Object> totalMap = new HashMap<>();
		
		// (1) 총 주식 자산
		Long totalStock = getTotalStock(userId);
		totalMap.put("totalStock", totalStock);
		// (2) 총 코인 자산
		Double totalCoin = getTotalCoin(userId);
		totalMap.put("totalCoin", totalCoin);
		// (3) 총 예적금
		Long totalDepositAndSavings = getTotalDepositAndSavings(userId);
		totalMap.put("totalDepositAndSavings", totalDepositAndSavings);
		// (4) 총 부동산
		Long totalApt = getTotalApt(userId);
		totalMap.put("totalApt", totalApt);
		// (5) 총 금, 외환
		Double totalGoldAndExchange = getTotalGoldAndExchange(userId);
		totalMap.put("totalGoldAndExchange", totalGoldAndExchange);
		// (6) 총 자동차
		Long totalCar = getTotalCar(userId);
		totalMap.put("totalCar", totalCar);
		// (7) 총 가계부잔액
		Integer totalAccountBalance = getTotalAccountBalance(userId);
		totalMap.put("totalAccountBalance", totalAccountBalance);
		// (8) 총 부채
		Long totalLiability = getTotalLiability(userId);
		totalMap.put("totalLiability", totalLiability);
		
		return totalMap; 
	}
	
	// (8) 총 부채 얻기
	public Long getTotalLiability(String userId) {
		Long totalLiability = 0L;
		
		UserDTO user = uRepo.findById(userId).get();
		List<UserLiabilityDTO> liabilityList = ulRepo.findByUser(user);
		for(int i=0; i<liabilityList.size(); i++) {
			UserLiabilityDTO dto = liabilityList.get(i);
			Long loanAmount = Long.parseLong(dto.getLoanAmount());
			totalLiability += loanAmount;
		}
		
		return totalLiability;
	}

	// (1) 총 주식 자산
	public Long getTotalStock(String userId) {
		String response = stockService.showHoldingStocks(userId); // userId
		JSONArray jsonArr = new JSONArray(response);

		Long total = 0L;
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject jsonObj = (JSONObject) jsonArr.get(i);
			Integer stockPrice = (Integer) jsonObj.get("stockPrice");
			Integer totalShares = (Integer) jsonObj.get("totalShares");
			Long eachStockAsset = (long) (stockPrice * totalShares);
			total += eachStockAsset;
		}
		System.out.println("총 주식 자산 : " + total);

		return total;
	}

	// (2) 총 코인 자산
	public Double getTotalCoin(String userId) {
		UserDTO user = uRepo.findById(userId).get();
		List<UserAssetDTO> coinList = uaRepo.findByUserAndAssetCode(user, coinAssetCode); // coinAssetCode == C2

		System.out.println("총 코인 List 수 : " + coinList.size());

		Double total = 0.0;
		for (int i = 0; i < coinList.size(); i++) {
			// 수량(quantity) 얻기
			UserAssetDTO coinAssetDto = coinList.get(i);
			Double quantity = Double.parseDouble(coinAssetDto.getQuantity());

			// 현재시세(prev_closing_price) 얻기 - 1)업비트, 2)빗썸
			String detailCode = coinAssetDto.getDetailCode();
			CoinUpbitDTO upbitCoin = upbitRepo.findById(detailCode).orElse(null);
			if (upbitCoin != null) { // ★ 가끔 코인이 상장폐지가 되는 경우가 있는데, null 처리 안해주면 나중에 에러날 수도
				String price = upbitCoin.getPrev_closing_price();
				Double prev_closing_price = Double.parseDouble(price);
				Double eachUpbitCoin = prev_closing_price * quantity;
				total += eachUpbitCoin;
			}
			CoinBithumbDTO bithumbCoin = bithumbRepo.findById(detailCode).orElse(null);
			if (bithumbCoin != null) { // ★ 가끔 코인이 상장폐지가 되는 경우가 있는데, null 처리 안해주면 나중에 에러날 수도
				String price = bithumbCoin.getPrev_closing_price();
				Double prev_closing_price = Double.parseDouble(price);
				Double eachBithumbCoin = prev_closing_price * quantity;
				total += eachBithumbCoin;
			}
		}
		System.out.println("총 코인 자산 : " + total);

		return total;
	}

	// (3) 총 예적금 자산
	public Long getTotalDepositAndSavings(String userId) {
		UserDTO user = uRepo.findById(userId).get();
		List<UserAssetDTO> depositAndSavingsList = uaRepo.findByUserAndAssetCodeStartingWith(user, "B"); // B1, B2

		System.out.println("총 예적금 List 수 : " + depositAndSavingsList.size());

		Long total = 0L;
		for (int i = 0; i < depositAndSavingsList.size(); i++) {
			UserAssetDTO depositAndSavingsDto = depositAndSavingsList.get(i);
			Long detailCode = Long.parseLong(depositAndSavingsDto.getDetailCode());
			DepositSavingsDTO depositSavingsDto = depositDtoRepo.findByDetailCode(detailCode);
			Integer price = Integer.parseInt(depositSavingsDto.getPrice());
			total += price;
		}
		System.out.println("총 예적금 자산 : " + total);

		return total;
	}

	// (4) 총 부동산 자산
	public Long getTotalApt(String userId) {
		UserDTO user = uRepo.findById(userId).get();
		List<UserAssetDTO> aptList = uaRepo.findByUserAndAssetCode(user, aptAssetCode); // aptAssetCode : E1

		Long total = 0L;
		for (int i = 0; i < aptList.size(); i++) {
			String detailCode = aptList.get(i).getDetailCode();
			Long tradeNo = Long.parseLong(detailCode);
			Long eachAptAsset = Long.parseLong(artRepo.findByTradeNo(tradeNo).getPrice());
			total += eachAptAsset;
		}
		System.out.println("총 아파트 자산 : " + total);

		return total;
	}

	// (5) 총 금, 외환 자산
	public Double getTotalGoldAndExchange(String userId) {
		// 1) 금
		String responseGold = goldService.showHoldingGold(userId);
		System.out.println(responseGold);

		Double total = 0.0;
		Long totalGold = 0L;
		JSONObject goldObj = new JSONObject(responseGold);
		Integer totalGram = (Integer) goldObj.get("totalGram");
		Integer miniGold = (Integer) goldObj.get("miniGold");
		totalGold = (long) (totalGram * miniGold);
		System.out.println("총 금 자산 : " + totalGold);

		String responseCurrency = currencyService.showHoldingCurrency(userId);
		JSONArray jsonArr = new JSONArray(responseCurrency);

		Double totalCurrency = 0.0D;
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject jsonObj = (JSONObject) jsonArr.get(i);
			Double marketPrice = Double.parseDouble(String.valueOf(jsonObj.get("marketPrice"))); // String.valueOf() : BigDecimal 어쩌고 에러 해결법
			Integer totalShares = (Integer) jsonObj.get("totalShares"); // totalShares : 보유 수
			
			String currency = (String) jsonObj.get("currency");
			if(currency.equals("jpy")) {
				Double eachCurrencyAsset = (marketPrice/100) * totalShares; // ★엔화는 100엔 기준이므로 계산시엔 100을 나눠주고
				totalCurrency += eachCurrencyAsset;
				System.out.println("엔화 자산 : " + totalCurrency);
			}else {
				Double eachCurrencyAsset = marketPrice * totalShares;
				totalCurrency += eachCurrencyAsset;
			}
		}
		System.out.println("총 외환 자산 : " + totalCurrency);

		total = totalGold + totalCurrency;

		return total;
	}

	// (6) 총 자동차 자산
	public Long getTotalCar(String userId) {
		UserDTO user = uRepo.findById(userId).get();
		List<UserAssetDTO> carList = uaRepo.findByUserAndAssetCode(user, carAssetCode); // carAssetCode : E2

		Long total = 0L;
		for (int i = 0; i < carList.size(); i++) {
			UserAssetDTO dto = carList.get(i);
			Long detailCode = Long.parseLong(dto.getDetailCode());
			Long price = Long.parseLong(carDtoRepo.findByDetailCode(detailCode).getPrice());
			total += price;
		}
		System.out.println("총 자동차 자산 : " + total);

		return total;
	}
	
	// (7) 총 가계부잔액
	public Integer getTotalAccountBalance(String userId) {
		// 거래내역 내림차순
		List<HouseholdAccountsDTO> accountBalanceList = haRepo.findByMemberIdOrderByExchangeDateDesc(userId);
		
		// 내림차순의 0번째를 통해 가장 최신 데이터 이용
		Integer balance = 0;
		if(accountBalanceList.size() != 0) {
			balance = accountBalanceList.get(0).getBalance();
		}
		System.out.println("총 가계부잔액 : " + balance);
		
		return balance;
	}

}
