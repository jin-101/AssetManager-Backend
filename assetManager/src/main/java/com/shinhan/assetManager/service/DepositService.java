package com.shinhan.assetManager.service;

import org.springframework.stereotype.Service;

import com.shinhan.assetManager.deposit.DepositDtoForReact;

@Service
public class DepositService implements AssetService {

	//예적금 추가 버튼 클릭시 => 
	public String addDeposit(DepositDtoForReact[] depositList) {
		String result = "";
		for(DepositDtoForReact de : depositList) {
			System.out.println(de.toString());
			//db에 넣는 로직
		}
		result = "예/적금 추가에 성공하였습니다.";
		return result;
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
