package com.shinhan.assetManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.shinhan.assetManager.service.DepositService;
import com.shinhan.assetManager.deposit.DepositDtoForReact;

@Controller
@RequestMapping("/deposit")
public class DepositController {
	
	@Autowired
	DepositService service;
	
	//예적금 추가 버튼 클릭시 => 
	@PostMapping(value = "/add.do")
	@ResponseBody
	public String addDeposit(@RequestBody DepositDtoForReact[] depositList) {
		String result = service.addDeposit(depositList);
		return result;
	}
		
}
