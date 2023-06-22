package com.shinhan.assetManager.apt;

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
public class AptAssetDTO {
	
	private Long currentPrice; // 현재가
	private Long purchasePrice; // 매입가
	private String rateOfReturn; // 수익률
	private String totalAvgRate; // 전체평균수익률

}