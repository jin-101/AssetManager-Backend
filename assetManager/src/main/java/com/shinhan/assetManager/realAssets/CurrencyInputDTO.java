package com.shinhan.assetManager.realAssets;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CurrencyInputDTO {
	
	private String userId;
	private String currency;
	private String price;
	private String buyDay;
	private String shares;
	

}
