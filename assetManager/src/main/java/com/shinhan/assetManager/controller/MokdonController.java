package com.shinhan.assetManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public Double calculate(@RequestBody MokdonDTO mokdonDto) {
		System.out.println("게산 요청 들어옴");
		System.out.println(mokdonDto);
		
		principal = service.calculate(mokdonDto);
		
		return principal; 
	}
	
	// 은행별 예적금 평균 금리 조회

}
