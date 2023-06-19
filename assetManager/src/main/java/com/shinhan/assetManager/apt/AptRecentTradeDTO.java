package com.shinhan.assetManager.apt;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter 
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apt_recent_trade")
@IdClass(MultikeyForAptRecent.class)
public class AptRecentTradeDTO {
	
	@Id
	private String aptName; // 아파트명
	@Id
	private String netLeasableArea; // 전용면적  
	@Id 
	private String areaCode; // 지역코드
	
	private Long tradeNo; // 세부코드를 위한 넘버
	
	private String dong; // 법정동 
	private String price; // 거래금액
	private String tradeDate; // 거래날짜 = 년+월
	
	// AptDTO의 @Id인 tradeNo를 FK로 가져야 할 듯????
	// private Long tradeNo; 

}
