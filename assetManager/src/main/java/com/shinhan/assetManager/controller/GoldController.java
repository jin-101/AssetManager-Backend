package com.shinhan.assetManager.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shinhan.assetManager.realAssets.GoldInputDTO;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;

@Controller
@RequestMapping("/gold")
public class GoldController {
	
	private String assetCode = "E3";
	private String detailCode = "Gold";
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserAssetRepo userAssetRepo;
	
	
	@PostMapping("/goldAssetInput")
	@ResponseBody
	public String handleGoldAssetsInputRequest(GoldInputDTO goldInputDTO) {
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
	
	
}
