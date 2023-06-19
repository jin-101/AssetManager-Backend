package com.shinhan.assetManager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.car.CrawlingSelenium;
import com.shinhan.assetManager.dto.CarCompanyDTO;
import com.shinhan.assetManager.dto.CarModelDTO;
import com.shinhan.assetManager.repository.CarCompanyRepository;
import com.shinhan.assetManager.repository.CarModelPRepository;
import com.shinhan.assetManager.repository.CarInfomationRepository;

@Service
public class CarService implements AssetService{

	@Autowired
	CarCompanyRepository carCompanyRepo;
	@Autowired
	CarModelPRepository carModelRepo;
	@Autowired
	CarInfomationRepository carInfoRepo;
	
	//차량번호로 조회 클릭시 => 
	public String searchMyCarAndInsert(String carNo) {
		CrawlingSelenium selenium = new CrawlingSelenium();
		System.out.println("carNo: "+ carNo);
		Map<String,String> obj = selenium.process(carNo);
		CarModelDTO dto= null;
		
		if(obj.get("className").equals("-") 
				|| obj.get("className").length()==0 
				|| obj.get("year").equals("-") 
				|| obj.get("year").length()==0) {
			return  "차량번호의 정보를 찾지 못했습니다. 직접 입력해주세요.";
		}
		
		//검색시 모델명 예외처리
		if(obj.get("className").indexOf("(")!=-1) {
			obj.put("className", obj.get("className").substring(0, obj.get("className").indexOf("(")));
		}
		
		//제조사명 얻기위해 dto 생성
		dto=carModelRepo.findFirstByClassNameContaining(obj.get("className"));
		if(dto == null) {
			return "차량의 제조사를 찾지 못했습니다. 직접 입력해주세요.";
		}
		
		// 연식이 있고 가격이 없는 경우 -> 차 이력테이블에서 제조사,모델명,연식에 맞는 가격 찾아서 넣는다.
		if(obj.get("price").equals("-") || obj.get("price").length()==0) {
			Integer price=carInfoRepo.getAveragePrice(dto.getCarCompany().getCompanyId(),obj.get("className"),obj.get("year"));
			obj.put("price", price+"");
		}
		
		//크롤링으로 가격정보가 들어온다면 ',' 없애기
		if(!obj.get("price").equals("-")) {
			obj.put("price", obj.get("price").replaceAll(",", ""));
		}
		
		//데이터 (모델명,가격,연식 / 제조사)
		System.out.println("제조사:"+ dto.getCarCompany().getCompanyName() +  ", 모델명:" +obj.get("className")+  ", 연식:" + obj.get("year") + ", 가격:"+ obj.get("price"));
		//데이터가 올바르게 넘어왔으면 저장은 여기서... 가격없어도 저장
	
		return "차량등록을 완료하였습니다.";
	}
	
	//직접입력 클릭시 => 
	public String carInsert(String carCompany, String carModel, String carYear) {
		System.out.println(carCompany +  " " +carModel+  " " +carYear);
		CarCompanyDTO carCo = carCompanyRepo.findByCompanyName(carCompany);
		
		CarModelDTO dto=carModelRepo.findFirstByClassNameContaining(carModel);
//		System.out.println(dto==null);
		
		Integer price=carInfoRepo.getAveragePrice(carCo.getCompanyId(),carModel,carYear);
		System.out.println("제조사:"+ carCompany +  ", 모델명:" +carModel+  ", 연식:" + carYear + ", 가격:"+ price);
		//데이터가 올바르게 넘어왔으면 저장은 여기서... 가격 없어도 저장
	
		return "차량등록을 완료하였습니다.";
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
