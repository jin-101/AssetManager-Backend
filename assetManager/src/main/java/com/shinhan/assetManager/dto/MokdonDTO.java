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

	// 공통
	private String targetAmount; // 목표금액
	private String targetPeriod; // 목표기간
	private String type; // 저축유형 - 자유롭게, 예금, 적금
	private String bank; // 선택한 은행 - 신한은행 등
	private String avgRate; // 평균금리
	private String rateType; // 단리, 복리 타입
	
	// 목돈 계산기
	private String requiredPrincipal; // 목표금액을 모으는 데 필요한 원금
	private String income; // 매달 저축금액
	private String totalPai; // 총 원리금
	private String lackingAmount; // 부족한 금액
	
	// 이자 계산기
	private String netIntr0_0; // 비과세
	private String netIntr15_4; // 일반 (15.4%)
	private String netIntr9_5; // 우대금리 (9.5%)
	private String netIntr1_4; // 우대금리 (1.4%)
}
