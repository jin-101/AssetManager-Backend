package com.shinhan.assetManager.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.apt.AptDtoForReact;
import com.shinhan.assetManager.service.AptService;

//React_WebBoardRestController 참조하여 만듦
@RestController 
@RequestMapping("/apt")
public class AptController {
	
	@Autowired
	AptService service;
	
	// 아파트 자산 추가
	@PostMapping(value = "/add/{token}", consumes = "application/json", produces = "text/plain;charset=utf-8")
	public String addApt(@RequestBody AptDtoForReact apt, @PathVariable String token) {
		System.out.println("리액트에서 들어온 아파트 자산 추가 : "+apt);
		String result = service.addApt(apt, token);
		System.out.println(apt);
		
		return result;
	}
	
	// 1. 시/도 선택시 => 구를 얻는 
	@GetMapping(value = "/getGu/{sido}") // consumes = "application/json" (★★넘기는 파라미터는 String인데, consumes는 JSON으로 지정해버리면 415 에러가 뜹니다~)
	@ResponseBody
	public Map<String, String> getGu(@PathVariable String sido) {
		System.out.println("리액트에서 선택한 Picker : "+sido);
		Map<String, String> guMap = service.getGu(sido);
		
		return guMap; 
	}
	
	// 2. 구 선택시 => 동/읍/면을 얻는
	@GetMapping(value = "/getDong/{gu}")
	@ResponseBody
	public Map<String, String> getDong(@PathVariable String gu) {
		Map<String, String> dongMap = service.getDong(gu);
		System.out.println(dongMap);
		
		return dongMap;
	}
	
	// 3. 동/읍/면 선택시 => 아파트 검색 가능하게끔
	@GetMapping(value = "/getAptName/{sido}/{gu}/{dong}")
	@ResponseBody
	public Map<String, String> getAptName(@PathVariable("sido") String sido,
										  @PathVariable("gu") String gu,
										  @PathVariable("dong") String dong) { // ★ @PathVariable 여려개일 때 지정해주는 법
		Map<String, String> aptMap = service.getAptName(sido, gu, dong);
		System.out.println(aptMap);
		
		return aptMap;
	}
	
	// 자산 탭 - 아파트 자산 조회 (수익률, 현재시세, 평단가)
}
