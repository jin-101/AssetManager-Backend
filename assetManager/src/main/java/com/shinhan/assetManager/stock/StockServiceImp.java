package com.shinhan.assetManager.stock;

import org.springframework.stereotype.Service;

import com.shinhan.assetManager.service.FinancialAssetService;

@Service
public class StockServiceImp implements FinancialAssetService{
	
	
	@Override
	public String getPrice(String assetCode, String detailCode) {
		//주식의 디테일코드는 종목코드
				
		
		return null;
	}

	@Override
	public String getReturn() {

		return null;
	}



}
