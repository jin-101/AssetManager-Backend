package com.shinhan.assetManager.dto;

import java.io.Serializable;
import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter@Setter@NoArgsConstructor@ToString
public class ExcelDTO {
	String detail_code;
	String 회원ID;
	String 계좌번호;
	String 은행;
	Date 거래일자;
	String 거래시간;
	Integer 출금;
	Integer 입금;
	String 내용;
	Integer 잔액;
	HouseholdAccountsCategoryDTO category;
	String memo;
	
}

 
