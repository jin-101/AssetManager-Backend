package com.shinhan.assetManager.apt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AptDtoForReact {
	
	private String sido;
	private String gu;
	private String dong;
	private String aptName;
	private String netLeasableArea;
	private String purchasePrice; // 매입가격
	private String quantity; // 수량
	
	// 대출 field
	private String loanAmount; // 대출금액
	private String rate; // 대출금리
	private String maturityDate; // 대출만기

}
