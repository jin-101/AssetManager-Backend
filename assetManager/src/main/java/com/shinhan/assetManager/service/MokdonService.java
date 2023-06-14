package com.shinhan.assetManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.dto.MokdonDTO;

@Service
public class MokdonService {

	@Autowired
	MokdonService service;
	Double principal = null; // 목표금액을 모으는 데 필요한 (총)원금
	Double interest = null; // 목표금액을 모으는 데 필요한 (총)이자
	String targetAmount = "";
	String targetPeriod = "";
	String type = "";
	String bank = "";
	String avgRate = "";
	String rateType = "";
	Double r0 = null; // 평균금리 (꼭 100으로 나뉜 값인지 체크할 것)
	Double r1 = null; // 기간 적용한 금리

	public Double calculate(MokdonDTO mokdonDto) {
		type = mokdonDto.getType();
		rateType = mokdonDto.getRateType();
		rateType = "복리"; // 테스트용
		if (type.equals("적금")) {
			if(rateType.equals("단리")) {
				principal = service.simpleInterestSavings(mokdonDto); // 단리
			}else if(rateType.equals("복리")) {
				principal = service.compoundInterestSavings(mokdonDto); // 복리
			}
		} else if (type.equals("예금")) {
			if(rateType.equals("단리")) {
				principal = service.simpleInterestDeposit(mokdonDto); // 단리
			}else if(rateType.equals("복리")) {
				principal = service.compoundInterestDeposit(mokdonDto); // 복리
			}
		}
		
		// ★ 15.4%, 9.5?% 세금 떼는 경우는 어떻게 해서 할까??
		return principal;
	}

	// 자유롭게 선택시

	// (1) 예금 - 단리
	public Double simpleInterestDeposit(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount();
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

		System.out.println("목돈 : " + principal);
		System.out.println("연이율(r0) : " + r0);
		System.out.println("기간적용이율(r1) : " + r1);

		return principal;
	}

	// (2) 적금 - 단리
	public Double simpleInterestSavings(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount();
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
		System.out.println("필요원금 : " + principal);
		System.out.println("총 이자율 : " + totalRate);
		System.out.println("총이자율 + 기간 : " + (totalRate + period));

		return principal;
	}

	// (3) 예금 - 복리 (월복리)
	public Double compoundInterestDeposit(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount();
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
		Double denominator = Math.pow((1+r1), period); // 분모
		principal = amount / denominator; // 필요원금 = 분자 / 분모

		System.out.println("필요원금 : " + principal);
		System.out.println("연이율(r0) : " + r0);
		System.out.println("월이율(r1) : " + r1);

		return principal;
	}

	// (4) 적금 - 복리 (월복리)
	public Double compoundInterestSavings(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount();
		targetPeriod = mokdonDto.getTargetPeriod();
		type = mokdonDto.getType();
		bank = mokdonDto.getBank();
		avgRate = mokdonDto.getAvgRate();
		avgRate = "10.0"; // ★ 평균금리를 얻었으면 => 계산할땐 100을 나눈 값으로 계산할 것
		Double rate = Double.parseDouble(avgRate);
		Integer period = Integer.parseInt(targetPeriod); // 목표기간
		Double amount = Double.parseDouble(targetAmount); // 목표금액
		
		// 계산식
		r0 = rate / 100; System.out.println("r0(연이율) : "+r0);
		r1 = r0 / 12; System.out.println("r1(월이율) : "+r1);
	    // 필요원금 계산식
		Double totalDenominator = 0.0;
		for(int i=period; i>0; i--) { 
			double j = (double) i;
			Double denominator = Math.pow((1+r1), j);
			totalDenominator += denominator;
		}
		//principal = (amount / (totalRate + period));
		principal = (amount / totalDenominator); // 단리 계산 때처럼 괄호 안의 기간 분리가 안되므로......
		System.out.println("필요원금 구하는데 필요한 분모값 : " + totalDenominator);
		System.out.println("필요원금 : " + principal);
		
		return principal;
	}

}
