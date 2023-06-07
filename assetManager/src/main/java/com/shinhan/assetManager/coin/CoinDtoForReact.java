package com.shinhan.assetManager.coin;

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
public class CoinDtoForReact {
	
	private String market;
	private String coinName;
	private String quantity;
	private String price;

}
