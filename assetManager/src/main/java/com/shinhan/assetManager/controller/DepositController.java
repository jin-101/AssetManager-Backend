package com.shinhan.assetManager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.service.DepositService;
import com.shinhan.assetManager.deposit.DepositDtoForReact;

@RestController
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
		
	//은행 리스트 받아오기
	@GetMapping(value = "/bankList.do")
	@ResponseBody
	public List<String> getBankList(){
		List<String> list = service.getBankList();
		return list;
	}
}
