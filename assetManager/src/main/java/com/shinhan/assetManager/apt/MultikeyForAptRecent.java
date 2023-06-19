package com.shinhan.assetManager.apt;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MultikeyForAptRecent implements Serializable {
	
	private String aptName; // 아파트명
	private String areaCode; // 지역코드
	private String netLeasableArea; // 전용면적

}
 