package com.shinhan.assetManager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.car.CrawlingSelenium;
import com.shinhan.assetManager.dto.CarCompanyDTO;
import com.shinhan.assetManager.repository.CarCompanyRepository;
import com.shinhan.assetManager.repository.CarModelPRepository;

@Service
public class CarService implements AssetService{

	@Autowired
	CarCompanyRepository carCompanyRepo;
	@Autowired
	CarModelPRepository carModelRepo;
	
	//차량번호로 조회 클릭시 => 
	public Map<String,String> searchMyCarAndInsert(String carNo) {
		CrawlingSelenium selenium = new CrawlingSelenium();
		System.out.println(carNo);
		Map<String,String> obj = selenium.process(carNo);
		System.out.println(obj);
		
		//데이터가 올바르게 넘어왔으면 저장은 여기서... => className으로 제조사 조회한다.
	
		return obj;
	}
	
	//직접입력 클릭시 => 
	public String carInsert(String carCompany, String carModel, String carYear) {
		System.out.println(carCompany +  " " +carModel+  " " +carYear);
		Map<String,String> obj = new HashMap<>();
		System.out.println(obj);
		
		//데이터가 올바르게 넘어왔으면 저장은 여기서... => 중고차 테이블에서 가격 조회하여 입력한다.
	
		return "입력성공";
	}
	
	//자동차 제조사리스트
	public List<String> getCompany() {
		List<String> list = new ArrayList<>();
		carCompanyRepo.findAll().forEach(company->{
			String co = company.getCompanyName();
			list.add(co);
		});
		return list;
	}
	//자동차 제조사 선택시 모델리스트
	public List<String> getModel(String companyName) {
		List<String> list = new ArrayList<>();
		CarCompanyDTO company= carCompanyRepo.findByCompanyName(companyName);
		carModelRepo.findByCarCompanyCompanyId(company.getCompanyId()).forEach(model -> {
			String mo = model.getClassName();
			list.add(mo);
		});
		return list;
	}
	
	@Override
	public String getPrice(String assetCode, String detailCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReturn() {
		// TODO Auto-generated method stub
		return null;
	}

}
