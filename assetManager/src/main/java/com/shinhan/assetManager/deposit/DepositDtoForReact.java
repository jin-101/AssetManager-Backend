package com.shinhan.assetManager.deposit;

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
public class DepositDtoForReact {
	private String depositType;
	private String bank;
	private String productName;
	private String startDate;
	private String endDate;
	private String price;
	private String rate;
}
