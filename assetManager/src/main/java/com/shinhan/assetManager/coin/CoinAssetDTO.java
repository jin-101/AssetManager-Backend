package com.shinhan.assetManager.coin;

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
public class CoinAssetDTO {
	
	private String market; // 거래소
	private String coinName; // 코인이름
	private Double currentPrice; // 현재가
	private Double totalPurchase; // 총 매입가
	private String avgPrice; // 평단가 (String : 소수점 때문에..)
	private Double quantity; // 수량
	private String rateOfReturn; // 수익률
	private String totalAvgRate; // 전체평균수익률

}
