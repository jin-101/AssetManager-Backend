package com.shinhan.assetManager.common;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.repository.AptRecentTradeRepo;
import com.shinhan.assetManager.repository.CoinBithumbRepo;
import com.shinhan.assetManager.repository.CoinUpbitRepo;
import com.shinhan.assetManager.repository.HouseholdAccountsRepository;
import com.shinhan.assetManager.repository.StockRepo;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserLiabilityRepo;
import com.shinhan.assetManager.service.StockService;

@Service
public class Redux {
	
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
	StockService stockService;
	
	public static void main(String[] args) {
		StockService stockService =  new StockService();
		String response = stockService.showHoldingStocks("jin"); // userId
		System.out.println(response);
		
		JSONArray jsonArr = new JSONArray(response);
		System.out.println(jsonArr);
	}
	
	public void getTotalAsset() {

	}

}
