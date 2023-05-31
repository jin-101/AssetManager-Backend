package com.shinhan.assetManager;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shinhan.assetManager.coin.ApiCoinBinance;
import com.shinhan.assetManager.coin.ApiCoinBithumb;
import com.shinhan.assetManager.coin.ApiCoinUpbit;
import com.shinhan.assetManager.coin.CoinBinanceDTO;
import com.shinhan.assetManager.coin.CoinBithumbDTO;
import com.shinhan.assetManager.coin.CoinUpbitDTO;
import com.shinhan.assetManager.repository.CoinBinanceRepo;
import com.shinhan.assetManager.repository.CoinBithumbRepo;
import com.shinhan.assetManager.repository.CoinUpbitRepo;

import lombok.extern.java.Log;

@Log
@SpringBootTest
class CoinTest {

	@Autowired
	CoinUpbitRepo up;
	@Autowired
	CoinBithumbRepo bit;
	@Autowired
	CoinBinanceRepo bin;
	
	@Autowired
	ApiCoinUpbit upbit;
	@Autowired
	ApiCoinBithumb bithumb;
	@Autowired 
	ApiCoinBinance binance;
	
	// 회원 김용희의 코인 손익 조회 (전체)
	//@Test
	void coin3() {
		// 1. 개인소유자산Table에서 자산코드 C2만 조회 => 그 회원이 소유하고 있는 코인 List로 받고
		
		// 2. 그 List로부터 코인의 손익을 종합하여 사용자에게 보여주기  
	}
	
	// 당일시세 insert => (O) 테스트 결과 문제 없음!
	@Test
	void coin1() throws IOException, InterruptedException {
		List<String> marketList = upbit.getMarketList();
		List<CoinUpbitDTO> coinUpbitList = upbit.getCoinPrice(marketList);
		List<CoinBithumbDTO> coinBithumbList = bithumb.getCoinPrice();
		List<CoinBinanceDTO> coinBinanceList = binance.getCoinPrice(); // 바이낸스는 일단 하지 않는 걸로
		
		coinBithumbList.forEach(coin->{
			bit.save(coin);
		});
		
		coinUpbitList.forEach(coin->{
			up.save(coin);
		});
		
		log.info("당일 코인 시세가 전부 입력 및 업데이트 되었습니다.");
	}
	
	// 당일시세 조회 => (O) 테스트 결과 문제 없음!
	//@Test
	void coin2() {
		// 1. 전체조회 (O)
//		up.findAll().forEach(coinUpbit->{
//			System.out.println(coinUpbit);
//		});
		
		
		// 2. 코인 이름으로 시세 조회 (O) (Repository에서 새 메소드 정의하였음)
		up.findByCoinNameStartingWith("BTC").forEach(coin->{
			System.out.println(coin.getCoinName());
			System.out.println(coin.getPrev_closing_price());
		});
		bit.findByCoinNameStartingWith("BTC").forEach(coin->{
			System.out.println(coin.getCoinName());
			System.out.println(coin.getPrev_closing_price());
		});
	}

}
