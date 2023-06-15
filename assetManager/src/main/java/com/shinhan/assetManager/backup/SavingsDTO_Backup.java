package com.shinhan.assetManager.backup;
//package com.shinhan.assetManager.dto;
//
//import java.sql.Date;
//import java.util.List;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//
//import org.springframework.format.annotation.DateTimeFormat;
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
//
//@Entity
//@Table(name = "savings")
//public class SavingsDTO_Backup {
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private String savingsNo;
//	
//	@OneToMany
//	@JoinColumn(name = "savings_id")
//	private List<SavingsOptionDTO> savingsOption; //적금상품id
//	
////	@ManyToOne
////	private FinancialCompanyDTO finCo; //금융회사정보
//	
//	@Column(name = "fin_co_no")
//	private String finCoNo; //금융회사정보
//	
//	@NonNull
//	private String finPrdtNm; //적금상품명
//	@NonNull
//	private String finPrdtCd; //금융상품 코드
//	@NonNull
//	private String joinWay;	//가입 방법
//	@NonNull
//	private String mtrtInt;	//만기 후 이자율 설명
//	@NonNull
//	private String spclCnd;	//우대조건
//	@NonNull
//	private String joinDeny; //가입제한 (1:제한없음, 2:서민전용, 3:일부제한)
//	@NonNull
//	private String joinMember; //가입대상
//	private String etcNote; //기타 유의사항
//	private Integer maxLimit; //최고한도 null 허용
////	@NonNull
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
//	private Date dclsStrtDay; //공시 시작일
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
//	private Date dclsEndDay; //공시 종료일 null 허용
//}