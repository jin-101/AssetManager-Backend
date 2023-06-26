package com.shinhan.assetManager.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.common.DecimalFormatForCurrency;
import com.shinhan.assetManager.dto.FinancialIndicatorDTO;
import com.shinhan.assetManager.dto.YearEndTaxDTO;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserLiabilityRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.repository.YearEndTaxRepository;
import com.shinhan.assetManager.user.AES256;
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
	AES256 aes256;
	@Autowired
	DecimalFormatForCurrency dfc;
	@Autowired
	TotalService totalService; // 총자산 얻기
	@Autowired
	YearEndTaxRepository yearEndTaxRepo; // 총소득 얻기
	
	// 모든 지표 얻는 메소드
	public FinancialIndicatorDTO getTotalIndicator(String userId) {
		// 총소득 얻기
		Integer salary = getSalary(userId);
		
		// 총자산 얻기
		Double totalAsset = totalService.getTotalAsset(userId);
		String totalAssetInString = dfc.currency(totalAsset);
		
		// 나이 얻기
		Integer age = getAge(userId);
		
		FinancialIndicatorDTO fiIndDto = new FinancialIndicatorDTO();
		fiIndDto = FinancialIndicatorDTO.builder()
				.salary(salary)
				.householdInd(getHouseholdInd(userId))
				.totalDebtRepaymentInd(getTotalDebtRepaymentInd(userId))
				.consumeDebtRepaymentInd(getConsumeDebtRepaymentInd(userId))
				.mortgageLoanRepaymentInd(getMortgageLoanRepaymentInd(userId))
				.totalDebtBurdenInd(getTotalDebtBurdenInd(userId, totalAsset))
				.mortgageLoanBurdenInd(getMortgageLoanBurdenInd(userId, totalAsset))
				.fiInvestInd(getFiInvestInd(userId, totalAsset))
				.fiAssetInd(getFiAssetInd(userId, totalAsset))
				.totalAsset(totalAssetInString)
				.age(age)
				.build();   
		getAge(userId);
		return fiIndDto;
	}
	
	// 나이 얻기
	public Integer getAge(String userId) {
		UserDTO user = userRepo.findById(userId).get();
		String encryptedSsn = user.getSsn();
		String salt = user.getSalt();
		String ssn = null;
		try {
			String decryptedSsn = aes256.decryptAES256(encryptedSsn);
			ssn = decryptedSsn.replace(salt, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Integer year = 0;
		Integer startNum = Integer.parseInt(ssn.substring(0,2));
		Integer endNum = Integer.parseInt(ssn.substring(7,8));
		if(endNum == 1 || endNum == 2) {
			year = 1900 + startNum;
		}else if(endNum == 3 || endNum == 4) {
			year = 2000 + startNum;
		}
		int todayYear = LocalDate.now().getYear();
		
		Integer age = todayYear - year + 1; 
		
		return age;
	}
	
	// 1-1. 가계수지지표 : 총지출 / 총소득
	public Double getHouseholdInd(String userId){ // 가계수지(household's total income and expenditure)
		// 총소득 얻기
		
		// 총지출 얻기 :
		
		return null;
	}
	
	// 2. 부채지표
	// 2-1. 총부채상환지표 : 총부채상환액 / 총소득
	public Double getTotalDebtRepaymentInd(String userId) { // 아마 파라미터로 userId 받을 듯?
		// 총소득 얻기
		
		// 총부채상환액 얻기 : user의 loanAmount * rate ????
		// (총부채상환액이란 '부채를 상환하기 위해 지출하는 모든 금액, 즉 모든 부채의 원리금상환액')
		UserDTO user = userRepo.findById(userId).get();
		List<UserLiabilityDTO> liabilityList = userLiabilityRepo.findByUser(user);
		Long totalLoanPrincipal = getLoanPrincipal(liabilityList); //원금
		
		// 지표 계산식
		
		
		return null;
	}
	 
	// 2-2. 소비생활부채상환지표 : 소비생활부채상환액 / 총소득
	public Double getConsumeDebtRepaymentInd(String userId) {
		// 총소득 얻기
		
		// 소비생활부채상환액 얻기 : ????
		UserDTO user = userRepo.findById(userId).get();
		List<UserLiabilityDTO> liabilityList = userLiabilityRepo.findByUserAndLiabilityCodeNot(user, "L1"); // ★ 메소드 바꿔줘야 함
		Long totalLoanPrincipal = getLoanPrincipal(liabilityList); //원금
		
		// 지표 계산식 
		
		return null;
	}
	
	// 2-3. 거주주택마련부채상환지표 : 거주주택마련부채상환액 / 총소득
	public Double getMortgageLoanRepaymentInd(String userId) {
		// 총소득 얻기
		
		// 거주주택마련부채상환액 얻기 : '원리금상환액'을 의미(잔액X), 만기일 데이터를 바탕으로 원리금 대략 계산해서 보여주면 될 듯!
		UserDTO user = userRepo.findById(userId).get();
		List<UserLiabilityDTO> liabilityList = userLiabilityRepo.findByUserAndLiabilityCode(user, "L1");
		Long totalLoanPrincipal = getLoanPrincipal(liabilityList); //원금
		
		return null;
	}
	
	
	// ★ 2-4 ~ 3-4 : '총자산'이 필요하므로 총자산을 얻는 공통 메소드 만들 생각
	// 2-4. 총부채부담지표 : 총부채 / 총자산
	public Double getTotalDebtBurdenInd(String userId, Double totalAsset) {
		// 총부채 얻기 : user 1개 이용해서 총 loanAmount 합산하면 될 듯
		UserDTO user = service.getUser(userId);
		List<UserLiabilityDTO> liabDtoList = userLiabilityRepo.findByUser(user);
		Double totalLoanAmount = 0.0;
		for(int i=0; i<liabDtoList.size(); i++) {
			UserLiabilityDTO liabDto = liabDtoList.get(i);
			Double loanAmount = Double.parseDouble(liabDto.getLoanAmount());
			totalLoanAmount += loanAmount;
		}
		
		// 지표계산
		Double abcd = totalLoanAmount / totalAsset;
		String indicator = dfc.decimalPlace1(abcd);
		System.out.println("총부채부담지표 : " + indicator);
		
		return Double.parseDouble(indicator);
	}
	
	// 2-5. 거주주택마련부채부담지표 : 거주주택마련부채잔액 / 총자산
	public Double getMortgageLoanBurdenInd(String userId, Double totalAsset) {
		// 주택마련부채 얻기 : '잔액'을 의미(원리금상환액X) user랑 liabilityCode 2개 이용해서 find하면 될 듯??
		UserDTO user = service.getUser(userId);
		List<UserLiabilityDTO> liabDtoList = userLiabilityRepo.findByUserAndLiabilityCode(user, "L1");
		Double totalLoanAmount = 0.0;
		for(int i=0; i<liabDtoList.size(); i++) {
			UserLiabilityDTO liabDto = liabDtoList.get(i);
			Double loanAmount = Double.parseDouble(liabDto.getLoanAmount());
			totalLoanAmount += loanAmount;
		}
		
		// 지표계산
		Double percent = totalLoanAmount / totalAsset;
		String indicator = dfc.decimalPlace1(percent);
		System.out.println("거주주택마련부채부담지표 : " + indicator);
		 
		return Double.parseDouble(indicator);
	}
	
	// 3-3. 금융투자성향지표 : 금융투자 / 저축 및 투자 
	public Double getFiInvestInd(String userId, Double totalAsset) {
		// 금융자산 얻기
		Double totalFinancialAsset = 0.0;
		Long totalStock = totalService.getTotalStock(userId);
		Double totalCoin = totalService.getTotalCoin(userId);
		Long totalDepositAndSavings = totalService.getTotalDepositAndSavings(userId);
		totalFinancialAsset = totalStock + totalCoin;
		
		// 지표계산
		Double numerator = totalFinancialAsset; // 분자
		Double denominator = totalStock + totalCoin + totalDepositAndSavings; // 분모 : 예적금, 펀드, 주식, 장기저축성보험, 노후대비를 위한 연금불입금 등
		String indicator = dfc.decimalPlace1(numerator/denominator);
		
		return Double.parseDouble(indicator);
	}
	
	// 3-4. 금융자산비중지표 : 금융자산 / 총자산
	public Double getFiAssetInd(String userId, Double totalAsset) {
		// 금융자산 얻기
		Double totalFinancialAsset = 0.0;
		Long totalStock = totalService.getTotalStock(userId);
		Double totalCoin = totalService.getTotalCoin(userId); 
		totalFinancialAsset = totalStock + totalCoin;
		
		// 지표계산
		Double percent = totalFinancialAsset / totalAsset;
		String indicator = dfc.decimalPlace1(percent);
		System.out.println("금융자산비중지표 : " + indicator);
		
		return Double.parseDouble(indicator);
	}

	
	// ■ 공통메소드
	// A. 총소득 얻기
	public Integer getSalary(String userId) {
		YearEndTaxDTO yetDto = yearEndTaxRepo.findByMemberId(userId);
		Integer salary = yetDto.getSalary(); // 세전소득
		
		return salary;
	}
	
	// B. userId로부터 UserDTO 얻기
	public UserDTO getUser(String userId) {
		// ID로부터 UserDTO 얻기 (★★UserAsset 엔티티의 user_id 부분이 UserDTO user 로 지정돼있으므로)
		UserDTO user = userRepo.findById(userId).get();
		
		return user;
	}
	
	// 부채상환 원금 구하기
	public Long getLoanPrincipal(List<UserLiabilityDTO> list) {
		Long totalLoanPrincipal = 0L;
		for(int i=0; i<list.size(); i++) {
			UserLiabilityDTO dto = list.get(i);
			Long maturityYear = Long.parseLong(dto.getLoanMaturity());
			Long loanAmount = Long.parseLong(dto.getLoanAmount());
			Long loanPrincipal = loanAmount / maturityYear;
			totalLoanPrincipal += loanPrincipal;
		}
		System.out.println("총 부채상환원금(1년) : " + totalLoanPrincipal);
		
		return totalLoanPrincipal;
	}
	// 부채상환 이자 구하기
	public Double getLoanInterest(List<UserLiabilityDTO> list) {
		Double totalLoanInterest = 0.0;
		for(int i=0; i<list.size(); i++) {
			UserLiabilityDTO dto = list.get(i);
			Long maturityYear = Long.parseLong(dto.getLoanMaturity());
			Long loanAmount = Long.parseLong(dto.getLoanAmount());
			Double rate = Double.parseDouble(dto.getRate()) / 100;
			Double loanInterest = loanAmount * rate;
			totalLoanInterest += loanInterest;
		}
		System.out.println("총 부채상환이자(1년) : " + totalLoanInterest);
		
		return totalLoanInterest;
	}
} 














