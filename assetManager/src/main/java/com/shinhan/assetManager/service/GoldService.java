package com.shinhan.assetManager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.realAssets.GoldInputDTO;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;

@Service
public class GoldService implements AssetService{
	
	private String assetCode = "E3";
	private String detailCode = "Gold";
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserAssetRepo userAssetRepo;
	
	
	public String registerGold(GoldInputDTO goldInputDTO) {
		String response = "등록완료";	
		
		try {
			
			System.out.println(goldInputDTO);
			Optional<UserDTO> user = userRepo.findById(goldInputDTO.getUserId());
		

			UserAssetDTO userAssetDto = new UserAssetDTO(user.get(), assetCode, detailCode, goldInputDTO.getPrice(), goldInputDTO.getBuyDay(), goldInputDTO.getBuyGram());
			
			userAssetRepo.save(userAssetDto);

		} catch (Exception e) {
			response = "등록실패";
		}
		
		return response;
	}
	
	public String showHoldingGold(String id) {
		
		Optional<UserDTO> user = userRepo.findById(id);
		List<UserAssetDTO> EveryUserGold = userAssetRepo.getSpecificUserAssets(user.get(), assetCode);
		
		long totalGram = 0L;
		long totalInvestedAmount = 0L;
		for(UserAssetDTO asset:EveryUserGold) {
			long gram = Long.parseLong(asset.getQuantity());
			long purchasePrice = Long.parseLong(asset.getPurchasePrice());
			long investedAmount = gram*purchasePrice;
			
			totalGram += gram;
			totalInvestedAmount += investedAmount;
			
		}
		
		double averagePriceByGram = (double) totalInvestedAmount/ totalGram;
		averagePriceByGram = Math.round(averagePriceByGram*1000)/1000.0;
		
		System.out.println(averagePriceByGram);
		
		
		
		
		return EveryUserGold.toString();
	}
	
	
	
	
	

	
	@Override
	public String getPrice(String market, String detailCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReturn() {
		// TODO Auto-generated method stub
		return null;
	}

}
