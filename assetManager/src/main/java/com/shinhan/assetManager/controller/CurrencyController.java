package com.shinhan.assetManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shinhan.assetManager.realAssets.CurrencyInputDTO;
import com.shinhan.assetManager.service.CurrencyService;

@Controller
@RequestMapping("/currency")
public class CurrencyController {
	
	@Autowired
	private CurrencyService currencyService;
	
	@PostMapping("/currencyAssetInput")
	@ResponseBody
	public String handleCurrencyInputRequest(CurrencyInputDTO currencyInputDTO) {
		String response = currencyService.registerCurrency(currencyInputDTO);
		

		return response;
	}
	
	@GetMapping("/currencyCrud")
	@ResponseBody
	public String handleShowHoldingCurrencyRequest(@RequestParam String id) {
		
		String response = currencyService.showHoldingCurrency(id);
		
		return response;
	}
	
	@GetMapping("/currencyGraph")
	@ResponseBody
	public String handleGraphRequest() {
		
		String response =  currencyService.getReadyGraph();
		
		return response;
	}

}
