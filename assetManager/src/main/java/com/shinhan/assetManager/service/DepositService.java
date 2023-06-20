package com.shinhan.assetManager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.deposit.DepositSavingsDTO;
import com.shinhan.assetManager.deposit.DepositDtoForReact;
import com.shinhan.assetManager.repository.DepositDTORepo;
import com.shinhan.assetManager.repository.FinancialCompanyRepository;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;

@Service
public class DepositService implements AssetService {

	private String assetDepositCode = "B1";
	private String assetSavingsCode = "B2";
	
	@Autowired
	FinancialCompanyRepository fiCoRepo;
	@Autowired
	DepositDTORepo depositDTOrepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private UserAssetRepo userAssetRepo;
	
	//예적금 추가 버튼 클릭시 => 
	public String addDeposit(DepositDtoForReact[] depositList, String userId) {
		String result = "";
		System.out.println(userId);
		Optional<UserDTO> user = userRepo.findById(userId);
		for(DepositDtoForReact de : depositList) {
			String code = de.getDepositType().equals("deposit") ? assetDepositCode : assetSavingsCode;
			System.out.println(code + " "+de.getDepositType());
			DepositSavingsDTO inputDepositInfo = DepositSavingsDTO.builder()
					.depositType(de.getDepositType())
					.bank(de.getBank())
					.productName(de.getProductName())
					.startDate(de.getStartDate())
					.endDate(de.getEndDate())
					.price(de.getPrice())
					.rate(de.getRate())
					.build();
			depositDTOrepo.save(inputDepositInfo);
			UserAssetDTO userAssetDto = new UserAssetDTO(user.get(), code,  Long.toString(inputDepositInfo.getDetailCode()), de.getPrice(), de.getStartDate(), "1");
			userAssetRepo.save(userAssetDto);
			System.out.println(de.toString());
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
