package com.shinhan.assetManager.apt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AptDtoForReact {
	
	private String sido;
	private String gu;
	private String dong;
	private String aptName;
	private String netLeasableArea;
	private String price;

}
