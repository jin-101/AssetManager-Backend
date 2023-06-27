package com.shinhan.assetManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shinhan.assetManager.service.StockService;
import com.shinhan.assetManager.stock.StockInputDTO;

@Controller
@RequestMapping("/stock")
public class StockController {
	
	
	@Autowired
	private StockService stockService;
	

	
	@PostMapping("/stockAssetInput")
	@ResponseBody
	public String handleStockAssetsInputRequest(StockInputDTO stockInputDTO) {
		
		String response = stockService.registerStock(stockInputDTO);
		
		return response;
	}
	
	
	@GetMapping("/stockCrud")
	@ResponseBody
	public String handleShowHoldingStockRequest(@RequestParam String id) {
		
		String response = stockService.showHoldingStocks(id);
		
		return response;
	}
	
	@GetMapping("/compareReturn")
	@ResponseBody
	public String handleCompareReturn(@RequestParam String id,@RequestParam String code,@RequestParam String market) {
		
		String response =stockService.compareReturn(id,code,market);
		
		return response;
	}
	
	@GetMapping("/flucRate")
	@ResponseBody
	public String handleYesterdayFlucRate(@RequestParam String pageMode) {
		String response =stockService.getPriceLimit(pageMode);
		
		return response;
	}
}
