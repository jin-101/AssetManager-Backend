package com.shinhan.assetManager.backup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class MokdonService_Backup {

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
	MokdonService_Backup service;
	Double principal = null; // 목표금액을 모으는 데 필요한 원금
	// Double interest = null; // 목표금액을 모으는 데 필요한 이자
	String targetAmount = "";
	String targetPeriod = "";
	String avgRate = "";
	String type = "";
	String bank = "";
	String rateType = "";
	Double r0 = null; // 평균금리 (꼭 100으로 나뉜 값인지 체크할 것)
	Double r1 = null; // 기간 적용한 금리
	Double d = 0.0;

	// 목돈 계산하기
	public MokdonDTO calculate(MokdonDTO mokdonDto) {
		mokdonDto.setRateType("단리"); // 테스트용
		type = mokdonDto.getType();
		rateType = mokdonDto.getRateType();
		if (type.equals("적금")) {
			if (rateType.equals("단리")) {
				mokdonDto = service.simpleInterestSavings(mokdonDto); // 단리
			} else if (rateType.equals("복리")) {
				mokdonDto = service.compoundInterestSavings(mokdonDto); // 복리
			}
		} else if (type.equals("예금")) {
			if (rateType.equals("단리")) {
				mokdonDto = service.simpleInterestDeposit(mokdonDto); // 단리
			} else if (rateType.equals("복리")) {
				mokdonDto = service.compoundInterestDeposit(mokdonDto); // 복리
			}
		}

		// ★ 15.4%, 9.5?% 세금 떼는 경우는 어떻게 해서 할까??
		return mokdonDto; 
	}
	
	// 은행별 예적금 평균 금리 조회
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
			for(int i=0; i<depositList.size(); i++) {
				int num = depositList.size();
				DepositDTO deposit = depositList.get(i);
				DepositOptionDTO depositOption = depositOptionRepo.findBySaveTrmAndDeposit(12, deposit);
				if(depositOption != null) {
					// 일단은.. 단복리 구분없이 금리만 얻자 (구분해서 평균내기엔 데이터 양도 좀 모자라고)
					Double intrRate = depositOption.getIntrRate();
					totalRateOfDeposit += intrRate;
					avgRateOfDeposit = totalRateOfDeposit / num;
				}
			}
			String depositAvgRate = String.format("%.2f", avgRateOfDeposit);
			// 3-1. 예금 계산 끝
			
			// 3-2. ★ 적금
			Double totalRateOfSavings = 0.0;
			Double avgRateOfSavings = 0.0;
			int count = 0; 
			for(int i=0; i<savingsList.size(); i++) {
				SavingsDTO savings = savingsList.get(i);  
				List<SavingsOptionDTO> soList1 = savingsOptionRepo.findBySavingsAndRsrvTypeNmAndSaveTrmBetween(savings, "자유적립식",6, 12);
				List<SavingsOptionDTO> soList2 = savingsOptionRepo.findBySavingsAndRsrvTypeNmAndSaveTrmBetween(savings, "정액적립식",6, 12);
				for(int j=0; j<soList1.size(); j++) {
					if(soList1.size() != 0) {
						SavingsOptionDTO so1 = soList1.get(j);
						if(so1 != null) {
							Double intrRate1 = so1.getIntrRate();
							totalRateOfSavings += intrRate1;
							count++; // 평균금리 구해서 토탈에 합할 때만 count
						}
					}
				}
				for(int k=0; k<soList2.size(); k++) {
					if(soList2.size() != 0) {
						SavingsOptionDTO so2 = soList2.get(k); // ★ 에러발생 : IndexOutOfBoundsException: Index 0 out of bounds for length 0 (k가 없는데 get(k)하라고 하니까 에러)
						if(so2 != null) {                                          
							Double intrRate2 = so2.getIntrRate();
							if(intrRate2 != null) {
								totalRateOfSavings += intrRate2;
								count++;
							}
						}
					}
				}
				//System.out.println("count : " + count);
				//avgRateOfSavings = totalRateOfSavings / count;
			}
			System.out.println("count : " + count);
			avgRateOfSavings = totalRateOfSavings / count;
			String savingsAvgRate = String.format("%.2f", avgRateOfSavings);
			// 3-2. 적금 계산 끝
			
			String korCoNm = finCo.getKorCoNm();
			if(finCo.getKorCoNm().contains("주식회사")) {
				korCoNm = finCo.getKorCoNm().replace("주식회사", "").trim(); // (1) 주식회사 단어 없애고, (2) 공백 없애기
			}
			System.out.println(korCoNm+"의 평균 적금금리 : "+savingsAvgRate);
			
			// 4. ★★★ 데이터 담기 - {은행명, 평균금리}
			AvgRateDTO ar = new AvgRateDTO(); 
			ar.setKorCoNm(korCoNm); // (1) {은행명, 평균금리}
			ar.setDepositAvgRate(depositAvgRate);
			ar.setSavingsAvgRate(savingsAvgRate);
			arList.add(ar); // (2) {} 객체를 List에 담기
			
			// 맵에 담기 테스트
			avgRateMap.put(korCoNm, ar); // (3) List<객체>를 Map에 담기
		}
		System.out.println("맵 테스트 : " + avgRateMap);
		
		return avgRateMap;
	}
	
	// 자유롭게 선택시

	// (1) 예금 - 단리
	public MokdonDTO simpleInterestDeposit(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount().replace(",", "");
		targetPeriod = mokdonDto.getTargetPeriod();
		type = mokdonDto.getType();
		bank = mokdonDto.getBank();
		avgRate = mokdonDto.getAvgRate();
		avgRate = "10.0"; // ★ 평균금리를 얻었으면 => 계산할땐 100을 나눈 값으로 계산할 것
		Double rate = Double.parseDouble(avgRate);
		Double period = Double.parseDouble(targetPeriod); // 목표기간
		Double amount = Double.parseDouble(targetAmount); // 목표금액

		// 계산식
		r0 = rate / 100; // %이므로 100으로 나누기
		r1 = (period / 12) * r0; // 연이율 환산
		principal = amount / (1 + r1); // ★예금계산
		String roundedPrincipal = getRoundedNum(principal);
		mokdonDto.setRequiredPrincipal(roundedPrincipal);

		return mokdonDto;
	}

	// (2) 적금 - 단리
	public MokdonDTO simpleInterestSavings(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount().replace(",", "");
		targetPeriod = mokdonDto.getTargetPeriod();
		type = mokdonDto.getType();
		bank = mokdonDto.getBank();
		avgRate = mokdonDto.getAvgRate();
		avgRate = "10.0"; // ★ 평균금리를 얻었으면 => 계산할땐 100을 나눈 값으로 계산할 것
		Double rate = Double.parseDouble(avgRate);
		Integer period = Integer.parseInt(targetPeriod); // 목표기간
		Double amount = Double.parseDouble(targetAmount); // 목표금액

		// 계산식
		r0 = rate / 100; // %이므로 100으로 나누기
		// r1 = (period/12)*r0; // 연이율 환산
		// principal = amount/(1+r1);

		// 총 이자율 계산
		Double totalRate = 0.0;
		Double eachRate = 0.0;
		for (int i = period; i > 0; i--) { // 12개월이면 12~1개월까지
			double j = (double) i; // ★★★ for문에서 double보다 int를 쓰는게 좋으니까 여기서 형변환 시켜버리는 게 나을 거 같은데??
			eachRate = ((j / 12) * r0);
			totalRate += eachRate;
		}
		principal = (amount / (totalRate + period));
		String roundedPrincipal = getRoundedNum(principal);
		mokdonDto.setRequiredPrincipal(roundedPrincipal);

		return mokdonDto;
	}

	// (3) 예금 - 복리 (월복리)
	public MokdonDTO compoundInterestDeposit(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount().replace(",", "");
		targetPeriod = mokdonDto.getTargetPeriod();
		type = mokdonDto.getType();
		bank = mokdonDto.getBank();
		avgRate = mokdonDto.getAvgRate();
		avgRate = "10.0"; // ★ 평균금리를 얻었으면 => 계산할땐 100을 나눈 값으로 계산할 것
		Double rate = Double.parseDouble(avgRate);
		Double period = Double.parseDouble(targetPeriod); // 목표기간
		Double amount = Double.parseDouble(targetAmount); // 목표금액

		// 계산식
		r0 = rate / 100; // 연이율
		r1 = (r0 / 12); // 월이율
		Double denominator = Math.pow((1 + r1), period); // 분모
		principal = amount / denominator; // 필요원금 = 분자 / 분모
		String roundedPrincipal = getRoundedNum(principal);
		mokdonDto.setRequiredPrincipal(roundedPrincipal);

		return mokdonDto;
	}

	// (4) 적금 - 복리 (월복리)
	public MokdonDTO compoundInterestSavings(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount().replace(",", "");
		targetPeriod = mokdonDto.getTargetPeriod();
		type = mokdonDto.getType();
		bank = mokdonDto.getBank();
		avgRate = mokdonDto.getAvgRate();
		avgRate = "10.0"; // ★ 평균금리를 얻었으면 => 계산할땐 100을 나눈 값으로 계산할 것
		Double rate = Double.parseDouble(avgRate);
		Integer period = Integer.parseInt(targetPeriod); // 목표기간
		Double amount = Double.parseDouble(targetAmount); // 목표금액

		// 계산식
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
		principal = (amount / totalDenominator); // 단리 계산 때처럼 괄호 안의 기간 분리가 안되므로......
		String roundedPrincipal = getRoundedNum(principal);
		mokdonDto.setRequiredPrincipal(roundedPrincipal);

		return mokdonDto;
	}

	// 공통 - 소수점 첫째자리까지 반올림
	public String getRoundedNum(Double principal) {
		String roundedPrincipal = String.format("%.1f", principal);
		System.out.println(roundedPrincipal);

		return roundedPrincipal;
	}
	
	// 은행별 예적금 평균 금리 조회 (땜빵용 ㅠㅠ)
	public Map<String, String> getAvgRatePlz() {
		Map<String, String> avgRateMap = new HashMap<>();
		
		// (1) 예금 평균금리 구한 뒤 Map에 
		Double totalIntr1 = 0.0;
		Double avgIntr1 = 0.0;
		int count1 = 0;
		
		Iterator<DepositOptionDTO> dIterator =	depositOptionRepo.findAll().iterator();
		while(dIterator.hasNext()) {
			DepositOptionDTO depositOption = dIterator.next();
			Double intrRate = depositOption.getIntrRate();
			if(intrRate != null) {
				totalIntr1 += intrRate;
				count1++;
			}
		}
		avgIntr1 = totalIntr1 / count1;
		String depositAvgIntr = String.format("%.2f", avgIntr1);
		avgRateMap.put("deposit", depositAvgIntr);
		
		
		// (2) 적금 평균금리 구한 뒤 Map에 
		Double totalIntr2 = 0.0;
		Double avgIntr2 = 0.0;
		int count2 = 0;
		
		Iterator<SavingsOptionDTO> sIterator =	savingsOptionRepo.findAll().iterator();
		while(sIterator.hasNext()) {
			SavingsOptionDTO savingsOption = sIterator.next();
			Double intrRate = savingsOption.getIntrRate();
			if(intrRate != null) {
				totalIntr2 += intrRate;
				count2++;
			}
		}
		avgIntr2 = totalIntr2 / count2;
		String savingsAvgIntr = String.format("%.2f", avgIntr2);
		avgRateMap.put("savings", savingsAvgIntr);
		
		return avgRateMap;
	}

}
