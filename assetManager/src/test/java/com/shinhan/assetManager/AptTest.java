package com.shinhan.assetManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shinhan.assetManager.apt.AdministrativeDistrictDTO;
import com.shinhan.assetManager.apt.ApiAptTrade;
import com.shinhan.assetManager.apt.AptDTO;
import com.shinhan.assetManager.apt.AptParamList;
import com.shinhan.assetManager.apt.AptRecentTradeDTO;
import com.shinhan.assetManager.repository.AdministrativeDistrictRepo;
import com.shinhan.assetManager.repository.AptRecentTradeRepo;
import com.shinhan.assetManager.repository.AptTradeRepo;

import lombok.extern.java.Log;

@Log
@SpringBootTest
public class AptTest {
	
	@Autowired
	AptTradeRepo tRepo;
	@Autowired
	AdministrativeDistrictRepo dRepo;
	@Autowired
	AptRecentTradeRepo artRepo; 
	
	@Autowired
	ApiAptTrade aptTrade;	
	
	@Test
	void selectAndInsertApt2() throws InterruptedException {
		Long startNo = 157034L;
		Long endNo = 167648L;
		List<AptDTO> aptList = tRepo.findByTradeNoBetween(startNo, endNo);
		for(int i=0; i<aptList.size(); i++) {
			Thread.sleep(100);
			AptDTO apt = aptList.get(i);
			Long tradeNo = apt.getTradeNo();
			String aptName = apt.getAptName();  
			String areaCode = apt.getAreaCode();
			String netLeasableArea = apt.getNetLeasableArea();
			String dong = apt.getDong();
			String price = apt.getPrice();
			String tradeDate = apt.getTradeDate();
			System.out.println(tradeNo + " " + aptName + " " + areaCode + " " + netLeasableArea);
			
			AptRecentTradeDTO artDto = AptRecentTradeDTO.builder()
					.tradeNo(tradeNo)
					.aptName(aptName)
					.areaCode(areaCode)
					.dong(dong)
					.netLeasableArea(netLeasableArea)
					.price(price)
					.tradeDate(tradeDate)
					.build();
			artRepo.save(artDto);
		}
	}

	
	// 0. apt_recent_trade 테스트 (★★) 
	//@Test
	void selectAndInsertApt() {  
		
		// (2) tradeNo 167649L(22년 1월 데이터)부터 시작
		List<AptDTO> aptList = tRepo.findByTradeNoGreaterThanEqual(405799L);
		for(int i=0; i<aptList.size(); i++) {
			AptDTO apt = aptList.get(i);
			Long tradeNo = apt.getTradeNo();
			String aptName = apt.getAptName(); 
			String areaCode = apt.getAreaCode();
			String netLeasableArea = apt.getNetLeasableArea();
			String dong = apt.getDong();
			String price = apt.getPrice();
			String tradeDate = apt.getTradeDate();
			System.out.println(tradeNo + " " + aptName + " " + areaCode + " " + netLeasableArea);
			
			AptRecentTradeDTO artDto = AptRecentTradeDTO.builder()
					.tradeNo(tradeNo)
					.aptName(aptName)
					.areaCode(areaCode)
					.dong(dong)
					.netLeasableArea(netLeasableArea)
					.price(price)
					.tradeDate(tradeDate)
					.build();
			artRepo.save(artDto);
		}
	}
	
	// 1. 아파트 리스트 insert (★★★)
	//@Test
	void insertAptTrade() throws InterruptedException {
		String[] areaCodeList = AptParamList.getAreaCodeList();
		List<String> dateList = AptParamList.getDateList();
		List<AptDTO> aptList = new ArrayList<>();
		
		// ★★★ 운영계정 키를 받게 된다면.. date도 달라지면서 insert하는 코드를 짜봐야겠는데 
		// (1번 할 때 1개월치를 넣을 수 있게끔 => 그래야 나중에 한달에 한번씩 추가할 때도 이 코드를 재사용하지)
		for(int i=1; i<dateList.size(); i++) { // for(int i=0; i<dateList.size(); i++)
			String date = dateList.get(i);
			System.out.println("★★★ 계약연월 : "+date);
			
			for(int j=0; j<areaCodeList.length; j++) { // for(int j=0; j<areaCodeList.length; j++)
				Thread.sleep(3000); 
				String areaCode = areaCodeList[j]; 
				String url = ApiAptTrade.urlBuild(areaCode, date); 
				String totalCount = ApiAptTrade.getTotalCount(url); 
				
				url += "&numOfRows="+totalCount; 
				System.out.println(url);
				
				aptList = ApiAptTrade.parsingXML(url, totalCount);
				aptList.forEach(apt->{
					tRepo.save(apt);
					System.out.println(apt.toString());
				});
				System.out.println("★★★ 계약연월 : "+date);
				System.out.println("------------------" + (j+1) + "번째 진행중" + "------------------"); 
			}
		}
	}
	
	
	// 2. 행정구역(district) insert (from apt_trade 테이블)
	// PK는 '동+구' 복합키로??
	//@Test
	void insertDistrict() {
		tRepo.findAll().forEach(apt->{
			System.out.println(apt);
			
			// 시군구(districtDTO) 입력
			String dong = apt.getDong();
			String areaCode = apt.getAreaCode();
			AdministrativeDistrictDTO district = new AdministrativeDistrictDTO();
			district.setDong(dong);
			district.setGu(areaCode); // 종로구면 => 11110을 set
			String sido = null; 
			String code = areaCode.substring(0, 2);
			switch(code) {
			case "11": sido = "서울특별시"; break;
			case "26": sido = "부산광역시"; break;
			case "27": sido = "대구광역시"; break;
			case "28": sido = "인천광역시"; break;
			case "29": sido = "광주광역시"; break;
			case "30": sido = "대전광역시"; break;
			case "31": sido = "울산광역시"; break;
			case "36": sido = "세종특별자치시"; break;
			case "41": sido = "경기도"; break;
			case "42": sido = "강원도"; break;
			case "43": sido = "충청북도"; break;
			case "44": sido = "충청남도"; break;
			case "45": sido = "전라북도"; break;
			case "46": sido = "전라남도"; break;
			case "47": sido = "경상북도"; break;
			case "48": sido = "경상남도"; break;
			case "50": sido = "제주도"; break;
			}
			district.setSido(sido);
			
			dRepo.save(district);
		});
	}
	
	
	//
	//@Test
	void tt() {
		List<String> guList = new ArrayList<>();
		String sido2 = "서울특별시";
		dRepo.findBySido(sido2).forEach(district->{
			guList.add(district.getGu());
		}); 
		System.out.println(guList);
	}
	
	// 3. 
	//@Test
	void test2() {
		// 1. 시/도를 고르면 => 구가 보이게끔
		dRepo.findBySido("서울특별시").forEach(district->{ // "서울특별시" 부분에 <= 리액트에서 select한 변수를 넣어줄 예정 
			log.info(district.getGu());
		});
		
		// 2. 구를 고르면 => 동이 보이게끔
		dRepo.findByGu("종로구").forEach(district->{
			log.info(district.getDong());
		});
		
		// 3. 동을 고르면 => 그에 해당하는 아파트 검색이 가능하게
		dRepo.findByDong("종로2동").forEach(district->{
			// log.info(district.getAptList);
		});
	}
	
	//@Test
	void dateTest() {
		String[] areaCodeList = AptParamList.getAreaCodeList();
		List<String> dateList = AptParamList.getDateList();
		
		System.out.println(dateList.get(0));
	}

}
