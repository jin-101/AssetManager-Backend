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
	@RequestMapping(value = "/mokdonCalculate", consumes = "application/json", produces = "application/json")
	public MokdonDTO mokdonCalculate(@RequestBody MokdonDTO mokdonDto) {
		mokdonDto = service.mokdonCalculate(mokdonDto);
		System.out.println(mokdonDto);
		
		return mokdonDto; 
	}
	
	// 이자 계산하기
	@PostMapping 
	@RequestMapping(value = "/interestCalculate", consumes = "application/json", produces = "application/json")
	public MokdonDTO interestCalculate(@RequestBody MokdonDTO mokdonDto) {
		mokdonDto = service.interestCalculate(mokdonDto);
		System.out.println(mokdonDto);
		
		return mokdonDto; 
	}
	
	// 은행별 예적금 평균 금리 조회 (원래 구현하려던 거)
	@GetMapping
	@RequestMapping(value = "/getAvgRate", produces = "application/json") 
	public Map<String, AvgRateDTO> getAvgRate() {
		Map<String, AvgRateDTO> avgRateMap = service.getAvgRate();
		
		return avgRateMap; 
	}

}
