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
public class MultikeyForDistrict implements Serializable {

	private String dong; // 동 (ex. 제기동)
	private String gu; // 구 (ex. 11110)
}
