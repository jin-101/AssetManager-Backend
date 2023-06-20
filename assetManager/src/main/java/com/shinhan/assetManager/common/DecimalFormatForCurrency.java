package com.shinhan.assetManager.common;

import java.text.DecimalFormat;

import org.springframework.stereotype.Component;

@Component 
public class DecimalFormatForCurrency {
	
	// 금액 콤마 표시
	public String currency(Double number) {
		DecimalFormat df = new DecimalFormat("#,###");
		String fmtNumber = df.format(number);
		
		return fmtNumber;
	}
	
	// 금액 콤마 표시
	public String currencyAndSymbol_10000(Double number) {
		DecimalFormat df = new DecimalFormat("\u00A4 #,###원");
		String fmtNumber = df.format(number*10000);
		
		return fmtNumber;
	}
	
	// 금액 콤마 표시 + 원화 표시
	public String currencyAndSymbol(Double number) {
		DecimalFormat df = new DecimalFormat("\u00A4 #,###원");
		String fmtNumber = df.format(number);
		
		return fmtNumber;
	}

}
