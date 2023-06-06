package com.shinhan.assetManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
		String result = null;
		
		// Insert에 성공하면 성공, 실패하면 실패를 React에 보내서 => 그에 따른 Alert 창을 보여주게끔 코드 짜야 함!!
		result = "성공";
		return result;
	}

}
