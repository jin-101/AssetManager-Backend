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
		if(type.equals("적금")) {
			principal = service.useSavings(mokdonDto); 
		}else if(type.equals("예금")) {
			principal = service.useDeposit(mokdonDto);
		}
		System.out.println("calculate에서 계산한 원금 : "+principal);
		
		// ★ 15.4%, 9.5?% 세금 떼는 경우는 어떻게 해서 할까??
		
		return principal;
	}
	
	// 자유롭게 선택시
	
	// 예금 선택시
	public Double useDeposit(MokdonDTO mokdonDto) {
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
		r0 = rate/100; // %이므로 100으로 나누기
		r1 = (period/12)*r0; // 연이율 환산
		principal = amount/(1+r1); // ★예금계산
		
		System.out.println("목돈 : "+principal);
		System.out.println("연이율(r0) : "+r0);
		System.out.println("기간적용이율(r1) : "+r1);
		
		return principal;
	}
	
	// 적금 선택시
	public Double useSavings(MokdonDTO mokdonDto) {
		targetAmount = mokdonDto.getTargetAmount();
		targetPeriod = mokdonDto.getTargetPeriod();
		type = mokdonDto.getType();
		bank = mokdonDto.getBank();
		avgRate = mokdonDto.getAvgRate();
		avgRate = "10.0"; // ★ 평균금리를 얻었으면 => 계산할땐 100을 나눈 값으로 계산할 것 
		Double rate = Double.parseDouble(avgRate);
		Integer period = Integer.parseInt(targetPeriod); // 목표기간
		Double amount = Double.parseDouble(targetAmount); // 목표금액
		System.out.println("목표기간 : "+period);
		
		// 계산식
		r0 = rate/100; // %이므로 100으로 나누기
		//r1 = (period/12)*r0; // 연이율 환산
		//principal = amount/(1+r1);
		
		// 총 이자율 계산
		Double totalRate = 0.0;
		Double eachRate = 0.0;
		for(int i=period; i>0; i--) { // 12개월이면 12~1개월까지
			double j = (double)i; // ★★★ for문에서 double보다 int를 쓰는게 좋으니까 여기서 형변환 시켜버리는 게 나을 거 같은데??
			System.out.println(j); // 10.0, 9.0, 8.0 ...
			eachRate = ((j/12)*r0);
			System.out.println("eachRate : "+eachRate);
			totalRate += eachRate;
		}
		System.out.println("총 이자율 : "+totalRate);
		
		// 목표금액/'총이자율+기간' == 필요한 원금?
		principal = (amount/(totalRate+period));
		System.out.println("총이자율 + 기간 : "+(totalRate+period));
		System.out.println("필요원금 : "+principal);
		
		return principal;
	}
	
	// 단리 계산
	public void simpleInterest() {
		
	}
	
	// 복리 계산
	public void compoundInterest() {
		
	}

}
