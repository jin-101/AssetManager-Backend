package com.shinhan.assetManager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.coin.CoinAssetDTO;
import com.shinhan.assetManager.coin.CoinBithumbDTO;
import com.shinhan.assetManager.coin.CoinDtoForReact;
import com.shinhan.assetManager.coin.CoinUpbitDTO;
import com.shinhan.assetManager.repository.CoinBithumbRepo;
import com.shinhan.assetManager.repository.CoinUpbitRepo;
import com.shinhan.assetManager.service.CoinService;

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
	@Autowired
	CoinService service;

	// 코인 자산 추가
	@PostMapping(value = "/add/{token}", consumes = "application/json", produces = "text/plain;charset=utf-8")
	public String addUpbit(@RequestBody CoinDtoForReact coin, @PathVariable String token) { //@PathVariable("id") String token, HttpServletRequest request
		System.out.println(coin);
		String result = service.addUpbit(coin, token);
  
		return result;
	}

	// 코인 검색 기능
	@PostMapping(value = "/getCoinList", consumes = "application/json") // produces = "application/json"
	@ResponseBody
	public Map<String, String> getCoinList(@RequestBody CoinDtoForReact coin) {
		Map<String, String> coinMap = new HashMap<>();
		coinMap = service.getCoinList(coin);
		
		return coinMap;
	}
	
	// 자산 탭 - 코인 자산 조회
	@GetMapping("/coinCrud")
	@ResponseBody
	public List<CoinAssetDTO> myCoinInfo(@RequestParam String userId) {
		List<CoinAssetDTO> response = service.myCoinInfo(userId);
		return response;
	}
}
