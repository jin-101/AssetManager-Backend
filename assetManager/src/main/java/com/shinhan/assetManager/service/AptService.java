package com.shinhan.assetManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.apt.AptDtoForReact;
import com.shinhan.assetManager.controller.AdministrativeDistrictGuRepo;
import com.shinhan.assetManager.repository.AdministrativeDistrictRepo;
import com.shinhan.assetManager.repository.AptTradeRepo;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;

@Service
public class AptService {
	
	@Autowired
	UserRepo uRepo;
	@Autowired
	AptTradeRepo tRepo;
	@Autowired
	AdministrativeDistrictRepo dRepo;
	@Autowired
	AdministrativeDistrictGuRepo guRepo;
	@Autowired
	UserAssetRepo assetRepo;
	
	// 아파트 자산 추가
	public String addApt(AptDtoForReact apt, String token) {
		UserDTO user = uRepo.findById(token).get();
		String purchasePrice = apt.getPurchasePrice().replace(",", "");
		
		// 자산 save
		UserAssetDTO assetDto = UserAssetDTO.builder()
				.user(user)
				.assetCode("E1")
				.detailCode("여기에 거래내역 테이블 ID를 넣어줘야 하는데..")
				.purchasePrice(purchasePrice)
				.purchaseDate(apt.getPurchaseDate())
				.quantity("1")
				.build();
		assetRepo.save(assetDto);
		
		return "성공";
	}

}
