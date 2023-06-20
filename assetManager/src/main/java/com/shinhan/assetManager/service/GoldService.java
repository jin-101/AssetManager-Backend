package com.shinhan.assetManager.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.realAssets.GoldInputDTO;
import com.shinhan.assetManager.realAssets.RealAssetDTO;
import com.shinhan.assetManager.repository.RealAssetsRepo;
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
	
	@Autowired
	private RealAssetsRepo realAssetRepo;
	
	
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
		
		//추후 최근 날짜로 로컬데이트 객체 수정
		Optional<RealAssetDTO>  realAssetDto =  realAssetRepo.findById(LocalDate.of(2023, 6, 9));
		RealAssetDTO realAsset = realAssetDto.get();
		
		double gold99k = realAsset.getGold99k();
		double minigold100g = realAsset.getMinigold100g();
		
		double returnBygold99k = (gold99k-averagePriceByGram)/averagePriceByGram;
		double returnByminiGold = (minigold100g-averagePriceByGram)/averagePriceByGram;
		
		returnBygold99k = Math.round(returnBygold99k*1000)/1000.0;
		returnByminiGold =  Math.round(returnByminiGold*1000)/1000.0;
		
		JSONObject userGold = new JSONObject();
		
		userGold.put("buyPriceByGram", averagePriceByGram);
		userGold.put("miniGold", minigold100g);
		userGold.put("gold99k",gold99k);
		userGold.put("returnBygold99k", returnBygold99k);
		userGold.put("returnByminiGold", returnByminiGold);
		
		
		
		return userGold.toString();
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
