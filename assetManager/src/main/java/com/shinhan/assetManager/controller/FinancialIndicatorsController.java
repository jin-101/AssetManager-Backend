package com.shinhan.assetManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.dto.FinancialIndicatorDTO;
import com.shinhan.assetManager.service.FinancialIndicatorsService;
import com.shinhan.assetManager.service.TotalService;
import com.shinhan.assetManager.user.UserDTO;

@RestController
public class FinancialIndicatorsController { 
	
	@Autowired
	FinancialIndicatorsService service;
	@Autowired
	TotalService totalService;
	
	// 각 회원의 재무지표 얻기
	@GetMapping(value = "/getFiInd", produces = "application/json") 
	public FinancialIndicatorDTO getFiInd(@RequestParam String userId) { 
		FinancialIndicatorDTO fiIndDto = service.getTotalIndicator(userId);
		service.getTotalDebtRepaymentInd(userId);
   
		return fiIndDto; 
	}

}
