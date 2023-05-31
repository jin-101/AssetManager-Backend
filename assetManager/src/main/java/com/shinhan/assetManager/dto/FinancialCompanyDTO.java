package com.shinhan.assetManager.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder 
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "financialCo")
public class FinancialCompanyDTO {
	@Id
	private String finCoNo; //금융회사코드
	@NonNull
	private String korCoNm; //금융회사이름
	
}
