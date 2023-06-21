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
public class MultikeyForUserLiability implements Serializable {

	private String user; // ★ 이것도 name을 서로 맞춰줘야 에러 안 나는 듯
	private String liabilityCode;
	private String detailCode; 
 
}
