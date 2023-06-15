package com.shinhan.assetManager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAndAvgRateDTO {
	
	private String korCoNm; // 은행명
	private Double avgRate; // 평균금리

}
 