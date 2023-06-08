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

@Controller
@RequestMapping("/stock")
public class StockController {
	
	@Autowired
	private StockRepo stockRepo;
	
	@Autowired
	private UserAssetRepo userAssetRepo;
	
	@PostMapping("/stockInfo")
	@ResponseBody
	public String getStockAssets(StockInputDTO stockInputDTO) {
		System.out.println("i got------------------------");
		System.out.println(stockInputDTO.getBuyDay());
		System.out.println(stockInputDTO.getStockName());
		System.out.println(stockInputDTO.getPrice());
		System.out.println(stockInputDTO.getShares());
		
		StockTableEntityMapping  map =  stockRepo.findByCorpname(stockInputDTO.getStockName());
		String stockCode = map.getStockcode();
		
		UserAssetDTO userAssetDto = new UserAssetDTO("godJin", "C1", stockCode, stockInputDTO.getPrice(), stockInputDTO.getBuyDay(), stockInputDTO.getShares());
		userAssetRepo.save(userAssetDto);
		
		return "잘받음";
	}
}
