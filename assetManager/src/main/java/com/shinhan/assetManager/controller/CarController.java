package com.shinhan.assetManager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.car.CrawlingSelenium;

@RestController
@RequestMapping("/car")
public class CarController {
		//예적금 추가 버튼 클릭시 => 
		@GetMapping(value = "/mySearch.do/{carNo}")
		@ResponseBody
		public Map<String,String> searchMyCar(@PathVariable String carNo) {
			CrawlingSelenium selenium = new CrawlingSelenium();
			System.out.println(carNo);
			Map<String,String> obj = selenium.process(carNo);
			System.out.println(obj);
			//데이터가 올바르게 넘어왔으면 저장은 여기서... => className으로 제조사 조회한다.
		
			
			return obj;
		}
		
}
