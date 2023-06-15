package com.shinhan.assetManager.backup;
//package com.shinhan.assetManager.dto;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.NonNull;
//
//@Data
//@Builder 
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "depositOption")
//public class DepositOptionDTO_Backup {
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)//oracle:sequence, mysql:identity
//	private Integer depositOptionId; // 예금상품 옵션 id
//	
//	//@ManyToOne
//	@Column(name = "deposit_id")
//	private String depositId; //예금상품 정보
//	
//	@NonNull
//	private String intrRateType; //저축 금리 유형
//	@NonNull
//	private String intrRateTypeNm; //저축 금리 유형명
//	@NonNull
//	private Integer saveTrm; //저축기간 
//	private Double intrRate; //저축 금리 [소수점 2자리]
//	private Double intrRate2; //최고 우대금리 [소수점 2자리]
//}