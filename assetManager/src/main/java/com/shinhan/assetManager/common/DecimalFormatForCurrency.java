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
	
	// % 표시 (소수점 첫째자리까지)
	public String percent(Double number) {
		DecimalFormat df = new DecimalFormat("#.#%");
		String fmtNumber = df.format(number);
		
		return fmtNumber;
	}
	
	// Double 소수점 첫째자리까지 
	public String decimalPlace1(Double number) {
		DecimalFormat df = new DecimalFormat("#.#");
		String fmtNumber = df.format(number*100); // %가 아니므로 직접 100 곱해줘야 하더라고
		
		return fmtNumber;
	}

}
