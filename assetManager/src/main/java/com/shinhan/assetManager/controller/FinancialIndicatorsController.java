package com.shinhan.assetManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.service.FinancialIndicatorsService;
import com.shinhan.assetManager.user.UserDTO;

@RestController
public class FinancialIndicatorsController { 
	
	@Autowired
	FinancialIndicatorsService service;
	
	@GetMapping(value = "/finRatio/{token}") // consumes = "application/json", produces = "text/plain;charset=utf-8" 
	public void addUpbit(@PathVariable("token") String userId) { 
		UserDTO user = service.getUser(userId);
		System.out.println("로그인한 userId : "+userId);
		
		service.bb(userId); 
		service.cc(userId); 
		service.dd(userId); 
  
		//return result;
	}

}
