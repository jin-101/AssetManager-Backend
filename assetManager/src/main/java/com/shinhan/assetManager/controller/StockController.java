package com.shinhan.assetManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shinhan.assetManager.mapping.StockTableEntityMapping;
import com.shinhan.assetManager.repository.StockRepo;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.stock.StockInputDTO;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;

@Controller
@RequestMapping("/stock")
public class StockController {
	
	private String assetCode = "C1";
	private UserDTO user;  
	
	@Autowired
	private StockRepo stockRepo;
	
	@Autowired
	private UserAssetRepo userAssetRepo;
	
	@PostMapping("/stockAssetInput")
	@ResponseBody
	public String handleStockAssetsInputRequest(StockInputDTO stockInputDTO) {
		System.out.println("i got------------------------");
		
		StockTableEntityMapping  map =  stockRepo.findByCorpname(stockInputDTO.getStockName());
		String stockCode = map.getStockcode();
		String market = map.getMarket();
		System.out.println(market);
		
		UserAssetDTO userAssetDto = new UserAssetDTO(user, assetCode, stockCode, stockInputDTO.getPrice(), stockInputDTO.getBuyDay(), stockInputDTO.getShares());
		userAssetRepo.save(userAssetDto);
		
		return "주식자산 등록완료";
	}
	
	public long handleStockPriceRequest() {
		
		return 0L;
	}
}
