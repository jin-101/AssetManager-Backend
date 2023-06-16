package com.shinhan.assetManager.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.dto.AvgRateDTO;
import com.shinhan.assetManager.dto.MokdonDTO;
import com.shinhan.assetManager.service.MokdonService;

@RestController
@RequestMapping("/mokdon")
public class MokdonController {
	
	@Autowired
	MokdonService service;
	Double principal = null; // 목표금액을 모으는 데 필요한 원금
	
	// 목돈 계산하기
	@PostMapping 
	@RequestMapping(value = "/calculate", consumes = "application/json", produces = "application/json")
	public MokdonDTO calculate(@RequestBody MokdonDTO mokdonDto) {
		System.out.println(mokdonDto);
		mokdonDto = service.calculate(mokdonDto);
		System.out.println("calculate에서 계산한 원금 : " + principal);
		
		return mokdonDto; 
	}
	
	// 은행별 예적금 평균 금리 조회 (땜빵용)
	@GetMapping
	@RequestMapping(value = "/getAvgRatePlz", produces = "application/json") // consumes = "application/json",
	public Map<String, String> getAvgRatePlz() {
		Map<String, String> avgRate = service.getAvgRatePlz(); 
		//Map<String, Object> bankAndAvgRate = service.getAvgRate();
		
		return avgRate; 
	}
	
	// 은행별 예적금 평균 금리 조회 (원래 구현하려던 거)
	@GetMapping
	@RequestMapping(value = "/getAvgRate", produces = "application/json") 
	public Map<String, AvgRateDTO> getAvgRate() {
		Map<String, AvgRateDTO> avgRateMap = service.getAvgRate();
		
		return avgRateMap; 
	}

}
