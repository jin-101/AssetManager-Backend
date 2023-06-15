package com.shinhan.assetManager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.service.CarService;

@RestController
@RequestMapping("/car")
public class CarController {
	
		@Autowired
		CarService service;
		
		//차량번호로 조회 클릭시 => 
		@PostMapping(value = "/mySearch.do/{carNo}")
		@ResponseBody
		public Map<String,String> searchMyCarAndInsert(@PathVariable String carNo) {
			Map<String,String> obj = service.searchMyCarAndInsert(carNo);
			System.out.println(obj); //obj를 넘겨서 front에서 정상 저장되었는지 판단한다.
		
			return obj;
		}
		
		//직접입력으로 클릭시 => 
		@PostMapping(value = "/insert.do/{carCompany}/{carModel}/{carYear}")
		@ResponseBody
		public String carInsert(
				@PathVariable("carCompany") String carCompany,
				@PathVariable("carModel") String carModel,
				@PathVariable("carYear") String carYear) {
			System.out.println(carCompany +  " " +carModel+  " " +carYear);
			String result = service.carInsert(carCompany, carModel, carYear);
			System.out.println(result);
			
			//데이터가 올바르게 넘어왔으면 저장은 여기서... => className으로 제조사 조회한다.
		
			return result;
		}
		
		//자동차 제조사리스트
		@GetMapping(value = "/companyList.do")
		@ResponseBody
		public List<String> getCompany() {
			List<String> list = service.getCompany();
			return list;
		}
		//자동차 제조사 선택시 모델리스트
		@GetMapping(value = "/modelList.do/{companyName}")
		@ResponseBody
		public List<String> getModel(@PathVariable String companyName) {
			List<String> list = service.getModel(companyName);
			return list;
		}
}
