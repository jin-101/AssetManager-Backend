package com.shinhan.assetManager.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor 
@Component
public class FinancialIndicatorDTO {
	
	private Integer age; // 나이
	private String totalAsset; // 총자산
	private Integer salary; // 총소득 
	
	private Double householdInd; // 가계수지지표 
	private Double totalDebtRepaymentInd; // 총부채상환지표
	private Double consumeDebtRepaymentInd; // 소비생활부채상환지표
	private Double mortgageLoanRepaymentInd; // 거주주택마련부채상환지표
	private Double totalDebtBurdenInd; // 총부채부담지표
	private Double mortgageLoanBurdenInd; // 거주주택마련부채부담지표
	private Double fiInvestInd; // 금융투자성향지표
	private Double fiAssetInd; // 금융자산비중지표

} 
