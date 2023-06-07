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
		System.out.println("axios로 넘어온 데이터: " + coin);

		String price = coin.getPrice().replace(",", "");
		String coinName = coin.getCoinName().toUpperCase(); // 대문자로 바꾸기 위한 작업
		System.out.println(coinName);
		System.out.println(price);

		String result = null;
		// Insert에 성공하면 성공, 실패하면 실패를 React에 보내서 => 그에 따른 Alert 창을 보여주게끔 코드 짜야 함!!
		result = "성공";

		return result;
	}

	@PostMapping(value = "/getCoinList", consumes = "application/json") // produces = "application/json"
	@ResponseBody
	public Map<String, String> getCoinList(@RequestBody CoinDtoForReact coin) {
		Map<String, String> coinMap = new HashMap<>();
		System.out.println(coin);
//		String market = coin.getMarket();

		// ★★★
		// market(업비트 or 빗썸)에 따라 검색되는 코인을 다르게 해주려고 했지만..
		// 그렇게 하려면 axios를 어떻게 써야될지 모르겠어서 일단 바로 데이터를 get할 수 있게 처리하였음.
		// 따라서 key값을 같은 BTC여도 업비트/빗썸 단어를 붙여서 다르게 주었음 (안 그러면 덮어씌워지더라고 key가 동일하니까)
		upbitRepo.findAll().forEach(upbit -> {
			String key = upbit.getCoinName().replace("_upbit", " : 업비트");
			String coinName = upbit.getCoinName().replace("_upbit", "");
			coinMap.put(key, coinName);
		});
		bitRepo.findAll().forEach(bithumb -> {
			String key = bithumb.getCoinName().replace("_bithumb", " : 빗썸");
			String coinName = bithumb.getCoinName().replace("_bithumb", "");
			coinMap.put(key, coinName);
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
