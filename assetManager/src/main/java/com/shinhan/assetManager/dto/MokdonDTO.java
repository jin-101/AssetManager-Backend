package com.shinhan.assetManager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MokdonDTO {

	private String targetAmount; // 목표금액
	private String targetPeriod; // 목표기간
	private String type; // 저축유형 - 자유롭게, 예금, 적금
	private String bank; // 선택한 은행 - 신한은행 등
	private String avgRate; // 평균금리
	private String rateType; // 단리, 복리 타입
	private Double principal; // 목표금액을 모으는 데 필요한 원금
}
