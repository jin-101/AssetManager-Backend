package com.shinhan.assetManager.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.car.CarDTO;
import com.shinhan.assetManager.dto.CarInfomationDTO;
import com.shinhan.assetManager.service.CarService;

@RestController
@RequestMapping("/car")
public class CarController {
	
		@Autowired
		CarService service;
		

		@GetMapping("/typeList")
		@ResponseBody
		public Set<String> carTypeList(@RequestParam String carCompany){
			Set<String> response = service.carTypeList(carCompany);
			return response;
		}
		@GetMapping("/recomand")
		@ResponseBody
		public List<CarInfomationDTO> carRecomand(
				@RequestParam String carCompany,
				@RequestParam String type,
				@RequestParam Integer minPrice,
				@RequestParam Integer maxPrice
				) {
			List<CarInfomationDTO> response = service.carRecomand(carCompany,type,minPrice,maxPrice);
			return response;
		}
		
		
		
		//소유 차량 정보
		@GetMapping("/carCrud")
		@ResponseBody
		public List<CarDTO> myCarInfo(@RequestParam String userId) {
			List<CarDTO> response = service.myCarInfo(userId);
			return response;
		}
		 
		//차량번호로 조회 클릭시 => 
		@PostMapping(value = "/mySearch.do/{carNo}/{userId}")
		@ResponseBody
		public String searchMyCarAndInsert(
				@PathVariable("carNo") String carNo,
				@PathVariable("userId") String userId) {
			String str = service.searchMyCarAndInsert(carNo, userId);
			System.out.println(str); //저장관련 멘트 전달
			return str;
		}
		
		//직접입력으로 클릭시 => 
		@PostMapping(value = "/insert.do/{carCompany}/{carModel}/{carYear}/{userId}")
		@ResponseBody
		public String carInsert(
				@PathVariable("carCompany") String carCompany,
				@PathVariable("carModel") String carModel,
				@PathVariable("carYear") String carYear,
				@PathVariable("userId") String userId) {
			System.out.println(carCompany +  " " +carModel+  " " +carYear);
			String result = service.carInsert(carCompany, carModel, carYear, userId);
			System.out.println(result); //저장관련 멘트 전달
			
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
