package com.shinhan.assetManager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.common.DecimalFormatForCurrency;
import com.shinhan.assetManager.dto.AvgRateDTO;
import com.shinhan.assetManager.dto.DepositDTO;
import com.shinhan.assetManager.dto.DepositOptionDTO;
import com.shinhan.assetManager.dto.FinancialCompanyDTO;
import com.shinhan.assetManager.dto.MokdonDTO;
import com.shinhan.assetManager.dto.SavingsDTO;
import com.shinhan.assetManager.dto.SavingsOptionDTO;
import com.shinhan.assetManager.repository.DepositOptionRepository;
import com.shinhan.assetManager.repository.DepositRepository;
import com.shinhan.assetManager.repository.FinancialCompanyRepository;
import com.shinhan.assetManager.repository.SavingsOptionRepository;
import com.shinhan.assetManager.repository.SavingsRepository;

@Service
public class MokdonService {

	@Autowired
	FinancialCompanyRepository fiCoRepo;
	@Autowired
	DepositRepository depositRepo;
	@Autowired
	DepositOptionRepository depositOptionRepo;
	@Autowired
	SavingsRepository savingsRepo;
	@Autowired
	SavingsOptionRepository savingsOptionRepo;
	@Autowired
	MokdonService service;
	@Autowired
	DecimalFormatForCurrency dfc;
	Double requiredPrincipal = null; // 목표금액을 모으는 데 필요한 원금
	String income = "";
	Integer incomes = null; // 
	Double lackingAmount = 0.0;
	String targetAmount = "";
	String targetPeriod = "";
	String avgRate = "";
	String type = "";
	String bank = "";
	String rateType = "";
	Double r0 = null; // 평균금리 (꼭 100으로 나뉜 값인지 체크할 것)
	Double r1 = null; // 기간 적용한 금리
	Double d = 0.0;

	// A. 목돈 계산하기
	public MokdonDTO mokdonCalculate(MokdonDTO mokdonDto) {
		type = mokdonDto.getType();
		rateType = mokdonDto.getRateType();
		
		// 일단 목돈계산은 단리만 적용
		if (type.equals("예금")) {
			mokdonDto = service.simpleInterestDeposit(mokdonDto); // 단리
		} else if (type.equals("적금")) {
			mokdonDto = service.simpleInterestSavings(mokdonDto); // 단리
		} 
		
		return mokdonDto; 
	}
	
	// A-1. 은행별 예적금 평균 금리 조회 (★변경사항 : 평균낼 때 저축금리(intrRate)말고 최고우대금리(intrRate2)로 변경하였음. 일반)
	// => Map보다 DTO 클래스를 사용해야 하는 이유 (참조: https://mangkyu.tistory.com/164)
	// => 필요한 데이터를 저장하기 위해 Map<String, Object>보다 => DTO를 이용하는 게 좋은 이유 
	public Map<String, AvgRateDTO> getAvgRate() {
		Map<String, AvgRateDTO> avgRateMap = new HashMap<>();
		List<AvgRateDTO> arList = new ArrayList<>();
		
		// 1. 각 은행 정보 얻기
		Iterator<FinancialCompanyDTO> finCoIterator = fiCoRepo.findAll().iterator();
		while(finCoIterator.hasNext()) {
			FinancialCompanyDTO finCo = finCoIterator.next();
			
			// 2. 그로부터 각 은행의 예금 List를 얻음
			List<DepositDTO> depositList = depositRepo.findByFinCo(finCo);
			List<SavingsDTO> savingsList = savingsRepo.findByFinCo(finCo);
			
			// 3-1. ★ 예금 List로부터 => 연이율(12개월)만 뽑아
			Double totalRateOfDeposit = 0.0;
			Double avgRateOfDeposit = 0.0;
			int count1 = 0; 
			for(int i=0; i<depositList.size(); i++) {
				DepositDTO deposit = depositList.get(i);
				List<DepositOptionDTO> doList = depositOptionRepo.findByDepositAndSaveTrmBetween(deposit, 6, 12);
				for(int j=0; j<doList.size(); j++) {
					DepositOptionDTO depositOption = doList.get(j); 
					if(depositOption != null) {
						// 일단은.. 단복리 구분없이 금리만 얻자 (구분해서 평균내기엔 데이터 양도 좀 모자라고)
						Double intrRate = depositOption.getIntrRate2();
						totalRateOfDeposit += intrRate;
						count1++; // 마찬가지로 합산할 때만 count 세기
					}
				}
			}
			avgRateOfDeposit = totalRateOfDeposit / count1;
			String depositAvgRate = String.format("%.2f", avgRateOfDeposit);
			// 3-1. 예금 계산 끝
			
			// 3-2. ★ 적금
			Double totalRateOfSavings = 0.0;
			Double avgRateOfSavings = 0.0;
			int count2 = 0; 
			for(int i=0; i<savingsList.size(); i++) {
				SavingsDTO savings = savingsList.get(i);  
				List<SavingsOptionDTO> soList1 = savingsOptionRepo.findBySavingsAndRsrvTypeNmAndSaveTrmBetween(savings, "자유적립식",6, 12);
				List<SavingsOptionDTO> soList2 = savingsOptionRepo.findBySavingsAndRsrvTypeNmAndSaveTrmBetween(savings, "정액적립식",6, 12);
				for(int j=0; j<soList1.size(); j++) {
					if(soList1.size() != 0) {
						SavingsOptionDTO so1 = soList1.get(j);
						if(so1 != null) {
							Double intrRate1 = so1.getIntrRate2();
							totalRateOfSavings += intrRate1;
							count2++; // 평균금리 구해서 토탈에 합할 때만 count
						}
					}
				}
				for(int k=0; k<soList2.size(); k++) {
					if(soList2.size() != 0) {
						SavingsOptionDTO so2 = soList2.get(k); // ★ 에러발생 : IndexOutOfBoundsException: Index 0 out of bounds for length 0 (k가 없는데 get(k)하라고 하니까 에러)
						if(so2 != null) {                                          
							Double intrRate2 = so2.getIntrRate2();
							if(intrRate2 != null) {
								totalRateOfSavings += intrRate2;
								count2++;
							}
						}
					}
				}
				//System.out.println("count : " + count);
				//avgRateOfSavings = totalRateOfSavings / count;
			}
			System.out.println("count : " + count2);
			avgRateOfSavings = totalRateOfSavings / count2;
			String savingsAvgRate = String.format("%.2f", avgRateOfSavings);
			// 3-2. 적금 계산 끝
			
			String korCoNm = finCo.getKorCoNm();
			if(finCo.getKorCoNm().contains("주식회사")) {
				korCoNm = finCo.getKorCoNm().replace("주식회사", "").trim(); // (1) 주식회사 단어 없애고, (2) 공백 없애기
			}
			System.out.println(korCoNm+"의 평균 예금금리 : "+depositAvgRate);
			System.out.println(korCoNm+"의 평균 적금금리 : "+savingsAvgRate);
			
			// 4. ★★★ 데이터 담기 - {은행명, 평균금리}
			AvgRateDTO arDto = new AvgRateDTO(); 
			arDto.setKorCoNm(korCoNm); // (1) {은행명, 평균금리}
			arDto.setDepositAvgRate(depositAvgRate);
			arDto.setSavingsAvgRate(savingsAvgRate);
			arList.add(arDto); // (2) {} 객체를 List에 담기
			
			// 맵에 담기 테스트
			avgRateMap.put(korCoNm, arDto); // (3) List<객체>를 Map에 담기
		}
		//★ 직접입력을 위한 코드
		AvgRateDTO arDto = new AvgRateDTO(); 
		arDto.setKorCoNm("직접입력");
		arDto.setDepositAvgRate("");
		arDto.setSavingsAvgRate("");
		avgRateMap.put("직접입력", arDto); 
		
		return avgRateMap;
	}

	// (A) 예금 - 단리
	public MokdonDTO simpleInterestDeposit(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount().replace(",", "");
		targetPeriod = mokdonDto.getTargetPeriod();
		type = mokdonDto.getType();
		bank = mokdonDto.getBank();
		avgRate = mokdonDto.getAvgRate();
		Double rate = Double.parseDouble(avgRate);
		Double period = Double.parseDouble(targetPeriod); // 목표기간
		Double amount = Double.parseDouble(targetAmount); // 목표금액

		// 목돈 계산식
		// (1) 필요원금 계산
		r0 = rate / 100; // %이므로 100으로 나누기
		r1 = (period / 12) * r0; // 연이율 환산
		requiredPrincipal = amount / (1 + r1); // ★ 필요원금 계산
		String roundedPrincipal = dfc.currencyAndSymbol_10000(requiredPrincipal); 
		mokdonDto.setRequiredPrincipal(roundedPrincipal);
		
		// (2) 총 원리금 및 부족액 계산
		income = mokdonDto.getIncome();
		if(!income.equals("")) {
			incomes = Integer.parseInt(income);
			Double principalAndInterest = incomes * (1 + r1);
			String pai = dfc.currencyAndSymbol_10000(principalAndInterest);
			mokdonDto.setTotalPai(pai);
			if(principalAndInterest >= amount) { // 저축 원리금 >= 목표금액의 경우
				mokdonDto.setLackingAmount("");
				mokdonDto.setRequiredPrincipal("");
			}else { // 저축 원리금 < 목표금액의 경우
				lackingAmount = amount - principalAndInterest;
				String la = dfc.currencyAndSymbol_10000(lackingAmount);
				
				requiredPrincipal = lackingAmount / (1 + r1); // ★ 필요원금 계산
				roundedPrincipal = dfc.currencyAndSymbol_10000(requiredPrincipal); 
				
				mokdonDto.setRequiredPrincipal(roundedPrincipal);
				mokdonDto.setLackingAmount(la);
			}
		}else {
			mokdonDto.setTotalPai("");
			mokdonDto.setLackingAmount("");
		}
		
		return mokdonDto;
	}

	// (A) 적금 - 단리
	public MokdonDTO simpleInterestSavings(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount().replace(",", "");
		targetPeriod = mokdonDto.getTargetPeriod();
		type = mokdonDto.getType();
		bank = mokdonDto.getBank();
		avgRate = mokdonDto.getAvgRate();
		Double rate = Double.parseDouble(avgRate);
		Integer period = Integer.parseInt(targetPeriod); // 목표기간
		Double amount = Double.parseDouble(targetAmount); // 목표금액

		// 목돈 계산식
		// (1) 필요원금 계산
		r0 = rate / 100; // %이므로 100으로 나누기
		Double totalRate = 0.0;
		Double eachRate = 0.0;
		for (int i = period; i > 0; i--) { // 12개월이면 12~1개월까지
			double j = (double) i; // ★★★ for문에서 double보다 int를 쓰는게 좋으니까 여기서 형변환 시켜버리는 게 나을 거 같은데??
			eachRate = ((j / 12) * r0);
			totalRate += eachRate;
		}
		requiredPrincipal = (amount / (totalRate + period));
		String roundedPrincipal = dfc.currencyAndSymbol_10000(requiredPrincipal);
		mokdonDto.setRequiredPrincipal(roundedPrincipal);
		
		// (2) 총 원리금 및 부족액 계산
		income = mokdonDto.getIncome(); 
		if(!income.equals("")) {
			incomes = Integer.parseInt(income);
			Double period2 = (double) period;
			Double interest = incomes * period2 * r0 * ((period2+1)/24); // 이자 계산
			Double principal = (double) (incomes * period);
			Double principalAndInterest = principal + interest;
			String pai = dfc.currencyAndSymbol_10000(principalAndInterest);
			mokdonDto.setTotalPai(pai);
			if(principalAndInterest >= amount) { // 저축 원리금 >= 목표금액의 경우
				mokdonDto.setLackingAmount("");
				mokdonDto.setRequiredPrincipal("");
			}else {
				lackingAmount = amount - principalAndInterest;
				String la = dfc.currencyAndSymbol_10000(lackingAmount);
				
				requiredPrincipal = (lackingAmount / (totalRate + period)); // ★ 필요원금 계산
				roundedPrincipal = dfc.currencyAndSymbol_10000(requiredPrincipal);
				
				mokdonDto.setRequiredPrincipal(roundedPrincipal);
				mokdonDto.setLackingAmount(la);
			}
		}else {
			mokdonDto.setTotalPai("");
			mokdonDto.setLackingAmount("");
		}

		return mokdonDto;
	}

	// (A) 예금 - 복리 (월복리)
	public MokdonDTO compoundInterestDeposit(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount().replace(",", "");
		targetPeriod = mokdonDto.getTargetPeriod();
		type = mokdonDto.getType();
		bank = mokdonDto.getBank();
		avgRate = mokdonDto.getAvgRate();
		Double rate = Double.parseDouble(avgRate);
		Double period = Double.parseDouble(targetPeriod); // 목표기간
		Double amount = Double.parseDouble(targetAmount); // 목표금액

		// 목돈 계산식
		r0 = rate / 100; // 연이율
		r1 = (r0 / 12); // 월이율
		Double denominator = Math.pow((1 + r1), period); // 분모
		requiredPrincipal = amount / denominator; // 필요원금 = 분자 / 분모
		String roundedPrincipal = dfc.currencyAndSymbol_10000(requiredPrincipal);
		mokdonDto.setRequiredPrincipal(roundedPrincipal);

		return mokdonDto;
	}

	// (A) 적금 - 복리 (월복리)
	public MokdonDTO compoundInterestSavings(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount().replace(",", "");
		targetPeriod = mokdonDto.getTargetPeriod();
		type = mokdonDto.getType();
		bank = mokdonDto.getBank();
		avgRate = mokdonDto.getAvgRate();
		Double rate = Double.parseDouble(avgRate);
		Integer period = Integer.parseInt(targetPeriod); // 목표기간
		Double amount = Double.parseDouble(targetAmount); // 목표금액

		// 목돈 계산식
		r0 = rate / 100;
		System.out.println("r0(연이율) : " + r0);
		r1 = r0 / 12;
		System.out.println("r1(월이율) : " + r1);
		// 필요원금 계산식
		Double totalDenominator = 0.0;
		for (int i = period; i > 0; i--) {
			double j = (double) i;
			Double denominator = Math.pow((1 + r1), j);
			totalDenominator += denominator;
		}
		// principal = (amount / (totalRate + period));
		requiredPrincipal = (amount / totalDenominator); // 단리 계산 때처럼 괄호 안의 기간 분리가 안되므로......
		String roundedPrincipal = dfc.currencyAndSymbol_10000(requiredPrincipal);
		mokdonDto.setRequiredPrincipal(roundedPrincipal);

		return mokdonDto;
	}

	// (A,B) 공통 메소드 - 소수점 ??째자리까지 반올림
	public String getRoundedNum(Double principal) {
		String roundedPrincipal = String.format("%.2f", principal*100); // *100 : 만원 => 원 (소수점 둘째자리로 하고 String으로 바꿔버리니까 자연스레 100이 곱해진 것처럼 돼버려서..)

		return roundedPrincipal;
	}
	
	// B. 이자 계산하기
	public MokdonDTO interestCalculate(MokdonDTO mokdonDto) {
		type = mokdonDto.getType(); // radioValue : 1, 2, 3, 4 가 넘어옴
		rateType = mokdonDto.getRateType();
		
		if (type.equals("적금")) {
			if(rateType.equals("단리")) {
				mokdonDto = service.calcIntrSiSavings(mokdonDto); // 예금 단리
			}else {
				mokdonDto = service.calcIntrCiSavings(mokdonDto); // 예금 복리
			}
		} else if (type.equals("예금")) {
			if(rateType.equals("단리")) {
				mokdonDto = service.calcIntrSiDeposit(mokdonDto); // 적금 단리
			}else {
				mokdonDto = service.calcIntrCiDeposit(mokdonDto); // 적금 복리
			}
		}

		return mokdonDto; 
	}
	
	// (B) 예금 - 단리
	public MokdonDTO calcIntrSiDeposit(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount().replace(",", "");
		targetPeriod = mokdonDto.getTargetPeriod();
		avgRate = mokdonDto.getAvgRate();
		type = mokdonDto.getType(); // 예금, 적금
		rateType = mokdonDto.getRateType(); // 단리, 복리
		Double arPercent = Double.parseDouble(avgRate);
		Integer period = Integer.parseInt(targetPeriod);
		Double amount = Double.parseDouble(targetAmount);
		
		Double ar = arPercent / 100;
		 
		// 이자 계산식
		Double interest = amount * ar * (period/12);
		String netIntr0_0 = taxation(interest, 0.0);
		String netIntr15_4 = taxation(interest, 15.4);
		String netIntr9_5 = taxation(interest, 9.5);
		String netIntr1_4 = taxation(interest, 1.4);
		
		mokdonDto.setNetIntr0_0(netIntr0_0);
		mokdonDto.setNetIntr15_4(netIntr15_4);
		mokdonDto.setNetIntr9_5(netIntr9_5);
		mokdonDto.setNetIntr1_4(netIntr1_4);
		
		return mokdonDto;
	}
	
	// (B) 예금 - 복리
	public MokdonDTO calcIntrCiDeposit(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount().replace(",", "");
		targetPeriod = mokdonDto.getTargetPeriod();
		avgRate = mokdonDto.getAvgRate();
		type = mokdonDto.getType(); // 예금, 적금
		rateType = mokdonDto.getRateType(); // 단리, 복리
		Double arPercent = Double.parseDouble(avgRate);
		Integer period = Integer.parseInt(targetPeriod);
		Double amount = Double.parseDouble(targetAmount);
		
		Double ar = arPercent / 100;
		
		// 이자 계산식
		Double rate = 1+(ar/12);
		Double totalAmount = Math.pow(rate, period) * amount;
		Double interest = totalAmount - amount;
		
		String netIntr0_0 = taxation(interest, 0.0);
		String netIntr15_4 = taxation(interest, 15.4);
		String netIntr9_5 = taxation(interest, 9.5);
		String netIntr1_4 = taxation(interest, 1.4);
		
		mokdonDto.setNetIntr0_0(netIntr0_0);
		mokdonDto.setNetIntr15_4(netIntr15_4);
		mokdonDto.setNetIntr9_5(netIntr9_5);
		mokdonDto.setNetIntr1_4(netIntr1_4);
		
		return mokdonDto;
	}
	
	// (B) 적금 - 단리
	public MokdonDTO calcIntrSiSavings(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount().replace(",", "");
		targetPeriod = mokdonDto.getTargetPeriod();
		avgRate = mokdonDto.getAvgRate();
		type = mokdonDto.getType();
		rateType = mokdonDto.getRateType(); 
		Double arPercent = Double.parseDouble(avgRate);
		Double period = Double.parseDouble(targetPeriod); // ★ 얘만 Integer가 아닌 Double
		Double amount = Double.parseDouble(targetAmount);
		
		Double ar = arPercent / 100;
		
		// 이자 계산식 (시중에 있는 적금 단리 계산 공식을 이용하였음 - 얘만 쫌 계산식이 다르더라고)
		// 참조 : https://twodongja.tistory.com/entry/%EC%A0%81%EA%B8%88-%EC%9D%B4%EC%9E%90-%EA%B3%84%EC%82%B0-%EC%A0%9C%EB%8C%80%EB%A1%9C-%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95
		Double interest = amount * period * ar * ((period+1)/24);
		
		String netIntr0_0 = taxation(interest, 0.0);
		String netIntr15_4 = taxation(interest, 15.4);
		String netIntr9_5 = taxation(interest, 9.5);
		String netIntr1_4 = taxation(interest, 1.4);
		
		mokdonDto.setNetIntr0_0(netIntr0_0);
		mokdonDto.setNetIntr15_4(netIntr15_4);
		mokdonDto.setNetIntr9_5(netIntr9_5); 
		mokdonDto.setNetIntr1_4(netIntr1_4);
	
	return mokdonDto;
	}
	
	// (B) 적금 - 복리
	public MokdonDTO calcIntrCiSavings(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount().replace(",", "");
		targetPeriod = mokdonDto.getTargetPeriod();
		avgRate = mokdonDto.getAvgRate();
		type = mokdonDto.getType(); // 예금, 적금
		rateType = mokdonDto.getRateType(); // 단리, 복리
		Double arPercent = Double.parseDouble(avgRate);
		Integer period = Integer.parseInt(targetPeriod);
		Double amount = Double.parseDouble(targetAmount);
		
		Double ar = arPercent / 100;
		Double monthlyRate = ar/12;
		Double totalAmount = 0.0;
		
		// 이자 계산식
		for(int i=period; i>0; i--) {
			double j = (double) i;
			Double eachAmount = amount * Math.pow((1+monthlyRate), j);
			totalAmount += eachAmount;
		}
		
		Double totalPrincipal = amount * period;
		Double interest = totalAmount - totalPrincipal; // totalPrincipal : 총 납입액 (달마다 넣으므로)
		
		String netIntr0_0 = taxation(interest, 0.0);
		String netIntr15_4 = taxation(interest, 15.4);
		String netIntr9_5 = taxation(interest, 9.5);
		String netIntr1_4 = taxation(interest, 1.4);
		
		mokdonDto.setNetIntr0_0(netIntr0_0);
		mokdonDto.setNetIntr15_4(netIntr15_4);
		mokdonDto.setNetIntr9_5(netIntr9_5); 
		mokdonDto.setNetIntr1_4(netIntr1_4);
	
	
	return mokdonDto;
	}
	
	// (B) 공통 메소드 - 세후 이자 계산
	public String taxation(Double interest, Double taxRate) {
		Double afterTaxInterest = interest * (1-(taxRate / 100));
		String netInterest = dfc.currencyAndSymbol_10000(afterTaxInterest);
		return netInterest;
	}

}
