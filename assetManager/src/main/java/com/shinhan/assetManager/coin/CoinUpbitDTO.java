package com.shinhan.assetManager.coin;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

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
@Table(name = "coin_upbit")
@Component
public class CoinUpbitDTO {
  
	@Id
	private String coinName;
	private String prev_closing_price; // 전일종가

	// private String market; // 조회할 코인명
}
