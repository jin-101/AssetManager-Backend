package com.shinhan.assetManager.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shinhan.assetManager.mapping.StockTableEntityMapping;
import com.shinhan.assetManager.realAssets.CurrencyInputDTO;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;

@Controller
@RequestMapping("/currency")
public class CurrencyController {
	//test
	private String assetCode = "A2";
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserAssetRepo userAssetRepo;
	
	@PostMapping("/currencyAssetInput")
	@ResponseBody
	public String handleCurrencyInputRequest(CurrencyInputDTO currencyInputDTO) {
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

}
