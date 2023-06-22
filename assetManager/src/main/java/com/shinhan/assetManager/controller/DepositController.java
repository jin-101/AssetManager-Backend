package com.shinhan.assetManager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.service.DepositService;
import com.shinhan.assetManager.deposit.DepositDtoForReact;
import com.shinhan.assetManager.deposit.DepositSavingsDTO;

@RestController
@RequestMapping("/deposit")
public class DepositController {
	
	@Autowired
	DepositService service;
	
	@DeleteMapping("depositDelete")
	public String depositDelete(@RequestParam String userId, @RequestParam String detailCode) {
		
		return "삭제완료 되었습니다.";
	}
	
	@GetMapping("/depositUpdate")
	@ResponseBody
	public List<DepositSavingsDTO> depositInfo(@RequestParam String userId){
		List<DepositSavingsDTO> list = service.depositInfo(userId);
		return list;
	}
	
	@GetMapping("/savingsUpdate")
	@ResponseBody
	public List<DepositSavingsDTO> savingsInfo(@RequestParam String userId){
		List<DepositSavingsDTO> list = service.savingsInfo(userId);
		return list;
	}
	
	@GetMapping("/depositCrud")
	@ResponseBody
	public Map<String,List<DepositSavingsDTO>> myDepositInfo(@RequestParam String userId) {
		Map<String,List<DepositSavingsDTO>> response = service.myDepositInfo(userId);
		return response;
	}
	
	//예적금 추가 버튼 클릭시 => 
	@PostMapping(value = "/add.do/{userId}")
	@ResponseBody
	public String addDeposit(
			@RequestBody DepositDtoForReact[] depositList, 
			@PathVariable("userId") String userId) {
		String result = service.addDeposit(depositList, userId);
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
