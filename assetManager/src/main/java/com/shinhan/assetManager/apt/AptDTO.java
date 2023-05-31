package com.shinhan.assetManager.apt;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Entity
@Table(name = "apt_trade")
public class AptDTO { // 필드 9개
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long tradeNo; // 
	
	private String areaCode; // 지역코드
	private String dong; // 법정동
	private String aptName; // 아파트명
	private String netLeasableArea; // 전용면적
	private String floors; // 층
	private String price; // 거래금액(만원)
	private String tradeDate; // 거래날짜 = 년+월
	private String constructionYear; // 건축년도
}

// [부동산영어] 실무를 하면서 많이 쓰는 영어단어 리스트 
// https://realestateagent-story.tistory.com/entry/%EB%B6%80%EB%8F%99%EC%82%B0%EC%98%81%EC%96%B4-%EC%8B%A4%EB%AC%B4%EB%A5%BC-%ED%95%98%EB%A9%B4%EC%84%9C-%EB%A7%8E%EC%9D%B4-%EC%93%B0%EB%8A%94-%EC%98%81%EC%96%B4%EB%8B%A8%EC%96%B4-%EB%A6%AC%EC%8A%A4%ED%8A%B8