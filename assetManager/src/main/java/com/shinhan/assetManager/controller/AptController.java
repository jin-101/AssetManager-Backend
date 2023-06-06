package com.shinhan.assetManager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.apt.AptDtoForReact;
import com.shinhan.assetManager.repository.AdministrativeDistrictRepo;
import com.shinhan.assetManager.repository.AptTradeRepo;

//React_WebBoardRestController 참조하여 만듦
@RestController 
@RequestMapping("/apt")
public class AptController {
	
	@Autowired
	AptTradeRepo aRepo;
	@Autowired
	AdministrativeDistrictRepo dRepo;
	
	// 아파트 자산 추가 버튼 클릭시 => 
	@PostMapping(value = "/add", consumes = "application/json", produces = "text/plain;charset=utf-8")
	public String addApt(@RequestBody AptDtoForReact apt) {
		System.out.println(apt);
		System.out.println(apt.getPrice().replace(",", "")); 
		String result = null;
		
		// Insert에 성공하면 성공, 실패하면 실패를 React에 보내서 => 그에 따른 Alert 창을 보여주게끔 코드 짜야 함!!
		result = "성공";
		return result;
	}
	
	// 시/도 선택시 => 구 리스트를 얻는 
	@PostMapping(value = "/getGu", consumes = "application/json")
	public List<String> getGu(@RequestBody AptDtoForReact apt) {
		List<String> guList = new ArrayList<>();
		System.out.println(apt.getGu());
		String sido = apt.getSido();
		dRepo.findBySido(sido).forEach(district->{
			guList.add(district.getGu());
		}); 
		System.out.println(guList);
		
		return guList; 
	}
}
