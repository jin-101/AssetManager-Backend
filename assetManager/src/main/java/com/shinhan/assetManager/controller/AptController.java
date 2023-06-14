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
import com.shinhan.assetManager.service.AptService;

//React_WebBoardRestController 참조하여 만듦
@RestController 
@RequestMapping("/apt")
public class AptController {
	
	@Autowired
	AptTradeRepo tRepo;
	@Autowired
	AdministrativeDistrictRepo dRepo;
	@Autowired
	AdministrativeDistrictGuRepo guRepo;
	@Autowired
	AptService service;
	
	// 아파트 자산 추가
	@PostMapping(value = "/add/{token}", consumes = "application/json", produces = "text/plain;charset=utf-8")
	public String addApt(@RequestBody AptDtoForReact apt, @PathVariable String token) {
		String result = service.addApt(apt, token);
		System.out.println(apt);
		
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
			String guName = guRepo.findByGu(gu).getGuName(); // 바로 String으로 리턴받을 순 없고, 단건에 해당하므로 DTO로 리턴받는 듯?? (여러건이면 List<DTO>이고)
			guMap.put(gu, guName);
		}); 
		//System.out.println(guMap);
		
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
	@GetMapping(value = "/getAptName/{sido}/{gu}/{dong}")
	@ResponseBody
	public Map<String, String> getAptName(@PathVariable("sido") String sido,
										  @PathVariable("gu") String gu,
										  @PathVariable("dong") String dong) { // ★ @PathVariable 여려개일 때 지정해주는 법
		
		Map<String, String> aptMap = new HashMap<>();
		System.out.println("리액트에서 넘어온 시/구/동 : "+sido+" "+gu+" "+dong);
		
		// ★ tRepo에 추가할 메소드 : (1)시 (2)구 (3)동 => 이 3가지 검색조건을 모두 만족하는 아파트 이름을 find
		tRepo.findByAreaCodeAndDong(gu, dong).forEach(apt->{ 
			//System.out.println(apt);
			aptMap.put(apt.getAptName()+"_"+apt.getNetLeasableArea(), apt.getAptName()); // 일단은 키에 아파트이름+전용면적, 밸류에 아파트이름 넣어봤음
		}); 
		
		System.out.println(aptMap);
		
		return aptMap;
	}
}
