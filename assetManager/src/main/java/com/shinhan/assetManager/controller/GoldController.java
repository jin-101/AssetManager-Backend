package com.shinhan.assetManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shinhan.assetManager.realAssets.GoldInputDTO;
import com.shinhan.assetManager.service.GoldService;

@Controller
@RequestMapping("/gold")
public class GoldController {
	
	
	@Autowired
	private GoldService goldService;
	
	
	@PostMapping("/goldAssetInput")
	@ResponseBody
	public String handleGoldAssetsInputRequest(GoldInputDTO goldInputDTO) {
		String response = goldService.registerGold(goldInputDTO);	
		
		return response;
	}
	
	@GetMapping("/goldCrud")
	@ResponseBody
	public String handleShowHoldingStockRequest(@RequestParam String id) {
		
		String response = goldService.showHoldingGold(id);
		
		return response;
	}
	
	@GetMapping("/goldGraph")
	@ResponseBody
	public String handleGraphRequest() {
		
		String response =  goldService.getReadyGraph();
		
		return response;
	}
	
	
}
