package com.shinhan.assetManager.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.dto.DepositDTO;
import com.shinhan.assetManager.dto.DepositOptionDTO;
import com.shinhan.assetManager.dto.FinancialCompanyDTO;
import com.shinhan.assetManager.dto.MokdonDTO;
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

	// 은행별 예적금 평균 금리 조회
	public Map<String, Object> getAvgRate() {
		Map<String, Object> bankAndAvgRate = new HashMap<>();

		// 1. 각 은행 정보를 얻음
		fiCoRepo.findAll().forEach(financialCompanyDto -> {
			FinancialCompanyDTO finCo = financialCompanyDto;

			// 2. 각 은행의 예금 List를 얻음
			List<DepositDTO> depositList = depositRepo.findByFinCo(finCo);

			// 3. ★ 그 예금 List로부터 => 12개월 +
			depositList.forEach(deposit -> {
				Double avgInterest = 0.0;
				System.out.println("deposit_id : " + deposit.getDepositId());
				// ★ 아.. 이게 12개월로 조건을 제한하니까 평균 계산에서 계속 오차가 나네.. (12개월만 있는 게 없는 것도 있어서)
				DepositOptionDTO depositOption = depositOptionRepo.findBySaveTrmAndDeposit(12, deposit);
				Double totalInterst = 0.0;
				if(depositOption != null) {
					Double intrRate = depositOption.getIntrRate();
					totalInterst += intrRate;
				}
				System.out.println("--------------------------------------");
				avgInterest = totalInterst / depositList.size();
				bankAndAvgRate.put(finCo.getKorCoNm(), avgInterest); // 은행명 - 평균금리
			});
		});

		return bankAndAvgRate;
	}

//	// 은행별 예적금 평균 금리 조회
//	public Map<String, Object> getAvgRate() {
//		Map<String, Object> bankAndAvgRate = new HashMap<>();
//
//		// 1. 각 은행 정보를 얻음
//		fiCoRepo.findAll().forEach(financialCompanyDto -> {
//			FinancialCompanyDTO finCo = financialCompanyDto;
//
//			// 2. 각 은행의 예금 List를 얻음
//			List<DepositDTO> depositList = depositRepo.findByFinCo(finCo);
//
//			// 3. ★ 그 예금 List로부터 => 12개월 +
//			depositList.forEach(deposit -> {
//				System.out.println("deposit_id : " + deposit.getDepositId());
//				// ★ 아.. 이게 12개월로 조건을 제한하니까 평균 계산에서 계속 오차가 나네.. (12개월만 있는 게 없는 것도 있어서)
//				// DepositOptionDTO depositOption =
//				// depositOptionRepo.findBySaveTrmAndDeposit(12, deposit);
//
//				List<DepositOptionDTO> depositOptionList = depositOptionRepo.findByDeposit(deposit);
//				int numOfDepositOption = depositOptionList.size();
//				System.out.println(finCo.getKorCoNm() + "의 예금 개수 : " + numOfDepositOption);
//				depositOptionList.forEach(depositOption -> {
//					Double avgInterest = 0.0;
//					Double totalInterst = 0.0;
//					System.out.println("deposit 옵션 아이디 : " + depositOption.getDepositOptionId());
//					Double intrRate = depositOption.getIntrRate();
//					String intrRateTypeNm = depositOption.getIntrRateTypeNm();
//					if (intrRate != null) {
//						int num = 0;
//						num++;
//						totalInterst += intrRate;
//					}
//					avgInterest = totalInterst / numOfDepositOption;
//					bankAndAvgRate.put(finCo.getKorCoNm(), avgInterest); // 은행명 - 평균금리
//				});
//				System.out.println("--------------------------------------");
//			});
//
//		});
//		;
//
//		return bankAndAvgRate;
//	}

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
		mokdonDto.setRoundedPrincipal(roundedPrincipal);

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
		mokdonDto.setRoundedPrincipal(roundedPrincipal);

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
		mokdonDto.setRoundedPrincipal(roundedPrincipal);

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
		mokdonDto.setRoundedPrincipal(roundedPrincipal);

		return mokdonDto;
	}

	// 공통 - 소수점 첫째자리까지 반올림
	public String getRoundedNum(Double principal) {
		String roundedPrincipal = String.format("%.1f", principal);
		System.out.println(roundedPrincipal);

		return roundedPrincipal;
	}

}
