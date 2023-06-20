package com.shinhan.assetManager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.realAssets.CurrencyInputDTO;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;

@Service
public class CurrencyService implements AssetService {
	
	private String assetCode = "A2";
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserAssetRepo userAssetRepo;
	
	
	public String registerCurrency(CurrencyInputDTO currencyInputDTO) {
		String response = "등록완료";	
		
		try {
			
			System.out.println(currencyInputDTO);
			Optional<UserDTO> user = userRepo.findById(currencyInputDTO.getUserId());
			UserAssetDTO userAssetDto = new UserAssetDTO(user.get(), assetCode, 
					currencyInputDTO.getCurrency(), currencyInputDTO.getPrice(), 
					currencyInputDTO.getBuyDay(), currencyInputDTO.getShares());
			
			userAssetRepo.save(userAssetDto);
		} catch (Exception e) {
			response = "등록실패";
		}
			
		return response;
		
	}
	
	public String showHoldingCurrency(String id) {
		Optional<UserDTO> user = userRepo.findById(id);
		
		List<UserAssetDTO> userCurrenciesWithUser = userAssetRepo.getSpecificUserAssets(user.get(),assetCode);
		
		for(UserAssetDTO asset:userCurrenciesWithUser) {
			System.out.println(asset.getDetailCode()+":"+asset.getPurchasePrice());
		}
		
		return userCurrenciesWithUser.toString();
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
