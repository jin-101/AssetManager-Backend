package com.shinhan.assetManager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvgRateDTO {
	
	private String korCoNm; // 은행명
	private String depositAvgRate; // 예금 평균금리 (은행별)
	private String savingsAvgRate; // 적금 평균금리 (은행별)
	private String rsrvTypeNm; // 자유적립식 & 정액적립식 (적립 유형)
	
	// 자유적립식, 정액적립식 각각의 평균금리는 그냥 나중에.. 시간 남으면 그때 분류..

}
