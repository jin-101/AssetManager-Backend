package com.shinhan.assetManager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.common.DecimalFormatForCurrency;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;

@Service
public class FinancialIndicatorsService { // 재무지표 (통계 탭 - 나의 재무지표 한눈에 보기)
	
	@Autowired
	FinancialIndicatorsService service;
	@Autowired
	UserAssetRepo userAssetRepo;
	@Autowired
	UserRepo userRepo;
	@Autowired
	DecimalFormatForCurrency dfc;
	
	// ★ 1-1 ~ 2-3 : '총소득'이 필요하므로 1년치 소득을 입력받을 컴포넌트가 필요할 듯.. (추가로 총소득을 얻는 공통 메소드 필요)
	
	// 1-1. 가계수지지표 : 총지출 / 총소득
	public void gg(String userId){ // 가계수지(household's total income and expenditure)
		// 총소득 얻기
		UserDTO user = service.getUser(userId);
		service.getTotalIncome(user);
		
		// 총지출 얻기 : 
	}
	
	// 2. 부채지표
	// 2-1. 총부채상환지표 : 총부채상환액 / 총소득
	public void aa(String userId) { // 아마 파라미터로 userId 받을 듯?
		// 총소득 얻기
		
		// 총부채상환액 얻기 : user의 loanAmount * rate ????
		// (총부채상환액이란 '부채를 상환하기 위해 지출하는 모든 금액, 즉 모든 부채의 원리금상환액')
		
		// ★ 1년치 총부채상환액 / 1년치 총소득 (=> 부득이한 경우라면 1년치가 아닌 1달치로 변경해서??)
	}
	 
	// 2-2. 소비생활부채상환지표 : 소비생활부채상환액 / 총소득
	public void ff(String userId) {
		// 총소득 얻기
		
		// 소비생활부채상환액 얻기 : ????
	}
	
	// 2-3. 거주주택마련부채상환지표 : 거주주택마련부채상환액 / 총소득
	public void ee(String userId) {
		// 총소득 얻기
		
		// 거주주택마련부채상환액 얻기 : '원리금상환액'을 의미(잔액X), 만기일 데이터를 바탕으로 원리금 대략 계산해서 보여주면 될 듯!
	}
	
	
	// ★ 2-4 ~ 3-4 : '총자산'이 필요하므로 총자산을 얻는 공통 메소드 만들 생각
	
	// 2-4. 총부채부담지표 : 총부채 / 총자산
	public void dd(String userId) {
		// 총자산 얻기
		UserDTO user = service.getUser(userId);
		Double totalAsset = service.getTotalAsset(user);
		
		// 총부채 얻기 : user 1개 이용해서 총 loanAmount 합산하면 될 듯
	}
	
	// 2-5. 거주주택마련부채부담지표 : 거주주택마련부채잔액 / 총자산
	public void bb(String userId) {
		// 총자산 얻기
		UserDTO user = service.getUser(userId);
		Double totalAsset = service.getTotalAsset(user);
		
		// 주택마련부채 얻기 : '잔액'을 의미(원리금상환액X) user랑 liabilityCode 2개 이용해서 find하면 될 듯??
	}
	
	// 3-3. 금융투자성향지표 : 금융투자 / 저축 및 투자 
	
	// 3-4. 금융자산비중지표 : 금융자산 / 총자산
	public void cc(String userId) {
		// 총자산 얻기
		UserDTO user = service.getUser(userId);
		Double totalAsset = service.getTotalAsset(user);
		
		// 금융자산 얻기 : user랑 AssetCode(C로 시작하는 놈들) 2개 이용해서 find하면 될 듯
	}

	
	// ■ 공통메소드
	// A. 총소득 얻기 (from ??)
	public void getTotalIncome(UserDTO user) {
		
	}
	
	// B. 총자산 얻기 (from user_asset 테이블)
	public Double getTotalAsset(UserDTO user) {
		// ★ (1) user_asset 테이블에 있는 총자산(원금) 그대로를 갖다 쓰는게 맞을까??
		//   (2) 아니면 현재 시세를 반영한 총자산을 쓰는게 맞을까??
		Double totalAsset = 0.0;
		List<UserAssetDTO> userAssetList = userAssetRepo.findByUser(user); 
		for(int i=0; i<userAssetList.size(); i++) {
			UserAssetDTO dto = userAssetList.get(i);
			Double purchasePrice = Double.parseDouble(dto.getPurchasePrice());
			Double quantity = Double.parseDouble(dto.getQuantity());
			Double subTotal = purchasePrice * quantity; // subTotal : 소계
			totalAsset += subTotal;
		}
		String totalAssetTest = dfc.currency(totalAsset);
		System.out.println(user.getUserId()+"의 총 자산 : "+totalAssetTest);
		
		return totalAsset;
	}
	
	// C. userId로부터 UserDTO 얻기
	public UserDTO getUser(String userId) {
		// ID로부터 UserDTO 얻기 (★★UserAsset 엔티티의 user_id 부분이 UserDTO user 로 지정돼있으므로)
		UserDTO user = userRepo.findById(userId).get();
		
		return user;
	}
} 














