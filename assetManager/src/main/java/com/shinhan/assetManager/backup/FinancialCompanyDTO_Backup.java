package com.shinhan.assetManager.backup;
//package com.shinhan.assetManager.dto;
//
//import java.util.List;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.OneToMany;
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
//
//@Entity
//@Table(name = "financialCo")
//public class FinancialCompanyDTO_Backup {
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private String fNo;
//	
//	@OneToMany
//	@JoinColumn(name = "fin_co_no") // 금융회사코드
//	private List<DepositDTO> deposit; // 예금쪽에 1:N
//	@OneToMany
//	@JoinColumn(name = "fin_co_no")
//	private List<SavingsDTO> savings; // 적금쪽에도 1:N
//	
//	@NonNull
//	private String korCoNm; //금융회사이름
//	 
//}
