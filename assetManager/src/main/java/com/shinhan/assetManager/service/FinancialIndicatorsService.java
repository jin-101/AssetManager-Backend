package com.shinhan.assetManager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.common.DecimalFormatForCurrency;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserLiabilityRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;
import com.shinhan.assetManager.user.UserLiabilityDTO;

@Service
public class FinancialIndicatorsService { // 재무지표 (통계 탭 - 나의 재무지표 한눈에 보기)
	
	@Autowired
	FinancialIndicatorsService service;
	@Autowired
	UserAssetRepo userAssetRepo;
	@Autowired
	UserLiabilityRepo userLiabilityRepo;
	@Autowired
	UserRepo userRepo;
	@Autowired
	DecimalFormatForCurrency dfc;
	@Autowired
	TotalService totalService; // 총자산 얻기
	
	Double loanAmount = 0.0;
	Double totalLoanAmount = 0.0;
	
	// String indicator; // 지표
	// Double loanAmount; // 대출금액
	
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
		UserDTO user = service.getUser(userId);
		
		// 거주주택마련부채상환액 얻기 : '원리금상환액'을 의미(잔액X), 만기일 데이터를 바탕으로 원리금 대략 계산해서 보여주면 될 듯!
		List<UserLiabilityDTO> liabDtoList = userLiabilityRepo.findByUserAndLiabilityCode(user, "L1");
		for(int i=0; i<liabDtoList.size(); i++) {
			UserLiabilityDTO liabDto = liabDtoList.get(i);
			liabDto.getLoanAmount();
			liabDto.getRate();
			liabDto.getMaturityDate();
			
			// ★ 이제 총 원리금 계산한 다음, 연도별로 보여주면 될 듯 
		}
	}
	
	
	// ★ 2-4 ~ 3-4 : '총자산'이 필요하므로 총자산을 얻는 공통 메소드 만들 생각
	
	// 2-4. 총부채부담지표 : 총부채 / 총자산
	public void dd(String userId) {
		// 총자산 얻기
		Double totalAsset = totalService.getTotalAsset(userId);
		
		// 총부채 얻기 : user 1개 이용해서 총 loanAmount 합산하면 될 듯
		UserDTO user = service.getUser(userId);
		List<UserLiabilityDTO> liabDtoList = userLiabilityRepo.findByUser(user);
		for(int i=0; i<liabDtoList.size(); i++) {
			UserLiabilityDTO liabDto = liabDtoList.get(i);
			loanAmount = Double.parseDouble(liabDto.getLoanAmount());
			totalLoanAmount += loanAmount;
		}
		
		// 지표계산
		Double abcd = totalLoanAmount / totalAsset;
		String indicator = dfc.percent(abcd);
		System.out.println("총부채부담지표 : " + indicator);
	}
	
	// 2-5. 거주주택마련부채부담지표 : 거주주택마련부채잔액 / 총자산
	public void bb(String userId) {
		// 총자산 얻기
		Double totalAsset = totalService.getTotalAsset(userId);
		
		// 주택마련부채 얻기 : '잔액'을 의미(원리금상환액X) user랑 liabilityCode 2개 이용해서 find하면 될 듯??
		UserDTO user = service.getUser(userId);
		List<UserLiabilityDTO> liabDtoList = userLiabilityRepo.findByUserAndLiabilityCode(user, "L1");
		for(int i=0; i<liabDtoList.size(); i++) {
			UserLiabilityDTO liabDto = liabDtoList.get(i);
			loanAmount = Double.parseDouble(liabDto.getLoanAmount());
			totalLoanAmount += loanAmount;
		}
		
		// 지표계산
		Double percent = totalLoanAmount / totalAsset;
		String indicator = dfc.percent(percent);
		System.out.println("거주주택마련부채부담지표 : " + indicator);
	}
	
	// 3-3. 금융투자성향지표 : 금융투자 / 저축 및 투자 
	public void hh(String userId) {
		
	}
	
	// 3-4. 금융자산비중지표 : 금융자산 / 총자산
	public void cc(String userId) {
		// 총자산 얻기
		Double totalAsset = totalService.getTotalAsset(userId);
		
		// 금융자산 얻기 : user랑 AssetCode(C로 시작하는 놈들) 2개 이용해서 find하면 될 듯
		Double totalFinancialAsset = 0.0;
		UserDTO user = service.getUser(userId);
		List<UserAssetDTO> assetDtoList = userAssetRepo.findByUserAndAssetCodeStartingWith(user, "C");
		for(int i=0; i<assetDtoList.size(); i++) {
			UserAssetDTO dto = assetDtoList.get(i);
			Double purchasePrice = Double.parseDouble(dto.getPurchasePrice());
			Double quantity = Double.parseDouble(dto.getQuantity());
			
			totalFinancialAsset = purchasePrice * quantity;
		}
		System.out.println("총 금융자산 : " + totalFinancialAsset);
		
		// 지표계산
		Double percent = totalFinancialAsset / totalAsset;
		String indicator = dfc.percent(percent);
		System.out.println("금융자산비중지표 : " + indicator);
	}

	
	// ■ 공통메소드
	// A. 총소득 얻기 (from ??)
	public void getTotalIncome(UserDTO user) {
		
	}
	
	// B. userId로부터 UserDTO 얻기
	public UserDTO getUser(String userId) {
		// ID로부터 UserDTO 얻기 (★★UserAsset 엔티티의 user_id 부분이 UserDTO user 로 지정돼있으므로)
		UserDTO user = userRepo.findById(userId).get();
		
		return user;
	}
} 














