package com.shinhan.assetManager.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.apt.AptDtoForReact;
import com.shinhan.assetManager.controller.AdministrativeDistrictGuRepo;
import com.shinhan.assetManager.repository.AdministrativeDistrictRepo;
import com.shinhan.assetManager.repository.AptTradeRepo;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserLiabilityRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;
import com.shinhan.assetManager.user.UserLiabilityDTO;

@Service
public class AptService implements AssetService {
	
	@Autowired
	UserRepo uRepo;
	@Autowired
	AptTradeRepo tRepo;
	@Autowired
	AdministrativeDistrictRepo dRepo;
	@Autowired
	AdministrativeDistrictGuRepo guRepo;
	@Autowired
	UserAssetRepo assetRepo;
	@Autowired
	UserLiabilityRepo liabilityRepo;
	
	// 아파트 자산 추가
	public String addApt(AptDtoForReact apt, String token) {
		StringBuilder sb = new StringBuilder();
		UserDTO user = uRepo.findById(token).get();
		String purchasePrice = apt.getPurchasePrice().replace(",", "");
		
		// 자산 save
		UserAssetDTO assetDto = UserAssetDTO.builder()
				.user(user)
				.assetCode("E1")
				.detailCode("여기에 거래내역 테이블 ID를 넣어줘야 하는데..") // ★★★
				.purchasePrice(purchasePrice)
				.purchaseDate(apt.getPurchaseDate())
				.quantity("1")
				.build();
		assetRepo.save(assetDto);
		sb.append("자산입력성공");
		
		// 부채 save
		String loanAmount = apt.getLoanAmount();
		String rate = apt.getRate();
		String maturityDate = apt.getMaturityDate();
		if(loanAmount != "" && rate != "" && maturityDate != "") {
			UserLiabilityDTO liabilityDto = UserLiabilityDTO.builder()
					.user(user)
					.liabiltyCode("주담대면 L1 이런 식으로")
					.detailCode("여기엔 주담대, 신용대출 등의 대출 이름을 기입")
					.loanAmount(apt.getLoanAmount())
					.rate(apt.getRate())
					.maturityDate(apt.getMaturityDate())
					.assetDetail(assetDto) // 부채(대출) 데이터를 넣을 때 => 위에서 생성한 자산 DTO도 같이 넣어주는 식
					.build();
			liabilityRepo.save(liabilityDto);
			sb.append(" + 부채입력성공");
		}
		
		return sb.toString();
	}
	
	// 1. 시/도 선택시 => 구를 얻는 
	public Map<String, String> getGu(String sido) {
		Map<String, String> guMap = new HashMap<>();
		dRepo.findBySido(sido).forEach(district->{
			String gu = district.getGu();
			String guName = guRepo.findByGu(gu).getGuName(); // 바로 String으로 리턴받을 순 없고, 단건에 해당하므로 DTO로 리턴받는 듯?? (여러건이면 List<DTO>이고)
			guMap.put(gu, guName);
		}); 
		
		return guMap; 
	}
	
	// 2. 구 선택시 => 동/읍/면을 얻는
	public Map<String, String> getDong(String gu) {
		Map<String, String> dongMap = new HashMap<>();
		dRepo.findByGu(gu).forEach(district->{ // ★ 유의 : 메소드가 다름 (findByGu != findBySido)
			String dong = district.getDong();
			dongMap.put(dong, "??");
		}); 
		
		return dongMap;
	}
	
	// 3. 동/읍/면 선택시 => 아파트 검색 가능하게끔
	public Map<String, String> getAptName(String sido, String gu, String dong) { // ★ @PathVariable 여려개일 때 지정해주는 법
		Map<String, String> aptMap = new HashMap<>();
		
		// ★ tRepo에 추가할 메소드 : (1)시 (2)구 (3)동 => 이 3가지 검색조건을 모두 만족하는 아파트 이름을 find
		tRepo.findByAreaCodeAndDong(gu, dong).forEach(apt->{ 
			aptMap.put(apt.getAptName()+"_"+apt.getNetLeasableArea(), apt.getAptName()); // 일단은 키에 아파트이름+전용면적, 밸류에 아파트이름 넣어봤음
		}); 
		
		return aptMap;
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
