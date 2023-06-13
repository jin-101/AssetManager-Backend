package com.shinhan.assetManager.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.car.CrawlingSelenium;
import com.shinhan.assetManager.repository.CarCompanyRepository;
import com.shinhan.assetManager.repository.CarModelPRepository;

import com.shinhan.assetManager.dto.CarCompanyDTO;

@RestController
@RequestMapping("/car")
public class CarController {
	
		@Autowired
		CarCompanyRepository carCompanyRepo;
		@Autowired
		CarModelPRepository carModelRepo;
		
		//차량번호로 조회 클릭시 => 
		@PostMapping(value = "/mySearch.do/{carNo}")
		@ResponseBody
		public Map<String,String> searchMyCarAndInsert(@PathVariable String carNo) {
			CrawlingSelenium selenium = new CrawlingSelenium();
			System.out.println(carNo);
			Map<String,String> obj = selenium.process(carNo);
			System.out.println(obj);
			
			//데이터가 올바르게 넘어왔으면 저장은 여기서... => className으로 제조사 조회한다.
		
			return obj;
		}
		
		//차량번호로 조회 클릭시 => 
		@PostMapping(value = "/insert.do/{carCompany}/{carModel}/{carYear}")
		@ResponseBody
		public Map<String,String> carInsert(
				@PathVariable("carCompany") String carCompany,
				@PathVariable("carModel") String carModel,
				@PathVariable("carYear") String carYear) {
			System.out.println(carCompany +  " " +carModel+  " " +carYear);
			Map<String,String> obj = new HashMap<>();
			System.out.println(obj);
			
			//데이터가 올바르게 넘어왔으면 저장은 여기서... => className으로 제조사 조회한다.
		
			return obj;
		}
		
		//자동차 제조사리스트
		@GetMapping(value = "/companyList.do")
		@ResponseBody
		public List<String> getCompany() {
			List<String> list = new ArrayList<>();
			carCompanyRepo.findAll().forEach(company->{
				String co = company.getCompanyName();
				list.add(co);
			});
			return list;
		}
		//자동차 제조사 선택시 모델리스트
		@GetMapping(value = "/modelList.do/{companyName}")
		@ResponseBody
		public List<String> getModel(@PathVariable String companyName) {
			List<String> list = new ArrayList<>();
			CarCompanyDTO company= carCompanyRepo.findByCompanyName(companyName);
			carModelRepo.findByCarCompanyCompanyId(company.getCompanyId()).forEach(model -> {
				String mo = model.getClassName();
				list.add(mo);
			});
			return list;
		}
}
