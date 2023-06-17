package com.shinhan.assetManager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.deposit.DepositDtoForReact;
import com.shinhan.assetManager.repository.FinancialCompanyRepository;

@Service
public class DepositService implements AssetService {

	@Autowired
	FinancialCompanyRepository fiCoRepo;
	
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
	
	//은행 리스트 받아오기
	public List<String> getBankList(){
		List<String> list = new ArrayList<>();
		fiCoRepo.findAll().forEach(bank ->{
			String b = bank.getKorCoNm();
			list.add(b);
		});
		return list;
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
