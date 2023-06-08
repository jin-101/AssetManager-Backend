package com.shinhan.assetManager.stock;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class StockInputDTO {
	private String stockName;
	private String price;
	private String buyDay;
	private String shares;
	
	

}
