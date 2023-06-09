package com.shinhan.assetManager.user;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class MultikeyForUser implements Serializable {
	
	// 복합키 (5개로 구성)
	private String userId;
	private String assetCode; // 자산코드
	private String detailCode; // 세부코드
	private String purchasePrice; // 매수가
	private String purchaseDate; // 매수일

}
