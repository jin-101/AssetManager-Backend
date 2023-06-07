package com.shinhan.assetManager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.shinhan.assetManager.repository.AdministrativeDistrictRepo;
import com.shinhan.assetManager.repository.AptTradeRepo;

//React_WebBoardRestController 참조하여 만듦
@RestController 
@RequestMapping("/apt")
public class AptController {
	
	@Autowired
	AptTradeRepo tRepo;
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
	
	// 1. 시/도 선택시 => 구를 얻는 
	@GetMapping(value = "/getGu/{sido}") // consumes = "application/json" (★★넘기는 파라미터는 String인데, consumes는 JSON으로 지정해버리면 415 에러가 뜹니다~)
	@ResponseBody
	public Map<String, String> getGu(@PathVariable String sido) {
		System.out.println("리액트에서 선택한 Picker : "+sido);
		
		Map<String, String> guMap = new HashMap<>();
		dRepo.findBySido(sido).forEach(district->{
			String gu = district.getGu();
			guMap.put(gu, "??"); // ★ 여기에 이제 구 이름을 넣어줘야겠네요 ㅅㅂ 언제 다 쓰냐 이거. 아마 이거 district 테이블 내용물도 바꿔줘야 할 거 같은데;;
		}); 
		System.out.println(guMap);
		
		return guMap; 
	}
	
	// 2. 구 선택시 => 동/읍/면을 얻는
	@GetMapping(value = "/getDong/{gu}")
	@ResponseBody
	public Map<String, String> getDong(@PathVariable String gu) {
		
		Map<String, String> dongMap = new HashMap<>();
		dRepo.findByGu(gu).forEach(district->{ // ★ 유의 : 메소드가 다름 (findByGu != findBySido)
			String dong = district.getDong();
			dongMap.put(dong, "??");
		}); 
		System.out.println(dongMap);
		
		return dongMap;
	}
	
	// 3. 동/읍/면 선택시 => 아파트 검색 가능하게끔
	@GetMapping(value = "/getAptName/{dong}")
	@ResponseBody
	public Map<String, String> getAptName(@PathVariable String dong) {
		Map<String, String> aptMap = new HashMap<>();
		System.out.println("리액트에서 넘어온 동 이름 : "+dong);
		
		// ★ tRepo에 추가할 메소드 : (1)시 (2)구 (3)동 => 이 3가지 검색조건을 모두 만족하는 아파트 이름을 find
		// ★ 
//		tRepo.findByDong(dong).forEach(district->{ // ★ 유의 : 메소드가 다름 (findByGu != findBySido)
//			String aptName = district.getDong()();
//			aptMap.put("", "??");
//		}); 
		
		System.out.println(aptMap);
		
		return aptMap;
	}
}
