package com.shinhan.assetManager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.coin.CoinBithumbDTO;
import com.shinhan.assetManager.coin.CoinDtoForReact;
import com.shinhan.assetManager.coin.CoinUpbitDTO;
import com.shinhan.assetManager.repository.CoinBithumbRepo;
import com.shinhan.assetManager.repository.CoinUpbitRepo;

// React_WebBoardRestController 참조하여 만듦
@RestController 
@RequestMapping("/coin")
public class CoinController {
	
	@Autowired
	CoinUpbitRepo upbitRepo;
	@Autowired
	CoinBithumbRepo bitRepo;
	@Autowired
	CoinUpbitDTO upbit;
	@Autowired
	CoinBithumbDTO bit;
	
	@PostMapping(value = "/add", consumes = "application/json", produces = "text/plain;charset=utf-8")
	public String addUpbit(@RequestBody CoinDtoForReact coin) {
		System.out.println(coin);
		System.out.println(coin.getPrice().replace(",", "")); 
		String result = null;
		
		// Insert에 성공하면 성공, 실패하면 실패를 React에 보내서 => 그에 따른 Alert 창을 보여주게끔 코드 짜야 함!!
		result = "성공";
		return result;
	}
	
	@PostMapping(value = "/getCoinList", consumes = "application/json") // produces = "application/json"
	@ResponseBody
	public Map<String, String> getCoinList(/*@RequestBody CoinDtoForReact coin*/) {
		Map<String, String> coinMap = new HashMap<>();
//		System.out.println(coin);
//		String market = coin.getMarket();
		
		upbitRepo.findAll().forEach(upbit->{
			String coinName = upbit.getCoinName().replace("_upbit", " : 업비트");
			coinMap.put(coinName, "밸류 아무거나 줘버려 걍");
		});
		bitRepo.findAll().forEach(bithumb->{
			String coinName = bithumb.getCoinName().replace("_bithumb", " : 빗썸");
			coinMap.put(coinName, "밸류 아무거나 줘버려 걍");
		});
		
//		if(market.equals("업비트")) { // ★ 또 .equals() 안 쓰고 == 쓰고 있네 (JS도 하다 보니까 ㅈㄴ 헷갈려 ㅡㅡ)
//			upbitRepo.findAll().forEach(upbit->{
//				String coinName = upbit.getCoinName().replace("_upbit", "");
//				coinMap.put(coinName, coinName);
//			});
//		}else if(market.equals("빗썸")) {
//			bitRepo.findAll().forEach(bithumb->{
//				String coinName = bithumb.getCoinName().replace("_bithumb", "");
//				coinMap.put(coinName, coinName);
//			});
//		}
		System.out.println(coinMap);
		return coinMap;
	}
}
