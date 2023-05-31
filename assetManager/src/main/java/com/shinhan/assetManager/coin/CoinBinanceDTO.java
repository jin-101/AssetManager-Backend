package com.shinhan.assetManager.coin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Entity
@Table(name = "coin_binance")
public class CoinBinanceDTO {
	
	@Id
	private String coinName; // 코인명 
	private String price; // 전일종가

	// symbol : (ex. BTCUSDT : 1USDT에 대한 1BTC 가격)
}
