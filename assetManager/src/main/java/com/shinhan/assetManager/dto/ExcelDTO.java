package com.shinhan.assetManager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter@Setter@NoArgsConstructor@ToString
public class ExcelDTO {
	private String 계좌번호; 
	private String 거래일자;
	private String 거래시간;
	private String 출금;
	private String 입금;
	private String 내용;
	private String 잔액;
}

 
