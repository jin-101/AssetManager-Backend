package com.shinhan.assetManager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CashReceiptExcelDTO {
	private String 거래일시;
	private String 가맹점명;
	private String 사용금액;
	private String 승인번호;
	private String 발급수단;
	private String 거래구분;
	private String 공제여부;
	private String 발행구분;
	private String 지출증빙_사업장;
}
