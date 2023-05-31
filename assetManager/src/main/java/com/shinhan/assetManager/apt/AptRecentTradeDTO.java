package com.shinhan.assetManager.apt;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apt_recent_trade")
public class AptRecentTradeDTO {
	
	@Id
	private Long no; // 
	
	private String areaCode; // 지역코드
	private String dong; // 법정동
	private String aptName; // 아파트명
	private String netLeasableArea; // 전용면적
	private String floors; // 층
	private String price; // 거래금액
	private String tradeDate; // 거래날짜 = 년+월
	private String constructionYear; // 건축년도
	
	// AptDTO의 @Id인 tradeNo를 FK로 가져야 할 듯????
	// private Long tradeNo; 

}
