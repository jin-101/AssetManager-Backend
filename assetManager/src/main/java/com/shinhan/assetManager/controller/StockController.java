package com.shinhan.assetManager.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shinhan.assetManager.mapping.StockTableEntityMapping;
import com.shinhan.assetManager.repository.StockRepo;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.service.StockService;
import com.shinhan.assetManager.stock.StockInputDTO;
import com.shinhan.assetManager.stock.StockTableEntity;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;

@Controller
@RequestMapping("/stock")
public class StockController {
	
	//수정 
	private String assetCode = "C1";

	
	@Autowired
	private StockRepo stockRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserAssetRepo userAssetRepo;
	
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
	public Map<String,Long> handleStockPriceRequest(@RequestParam String id) {
		
		System.out.println(id);
		
		
		Optional<UserDTO> user = userRepo.findById(id);
		
		List<UserAssetDTO> userStocksWithUser = userAssetRepo.getSpecificUserAssets(user.get(),assetCode);
		
//		for(int i=0; i<userStocks.size();i++) {
//			System.out.println(userStocks.get(i));
//		}
		
		
		Map<String, Long> totalSharesByStockCode = new HashMap<>();
		Map<String, Long> totalAmountByStockCode = new HashMap<>();
		Map<String, Long> averageStockPriceByStockName = new HashMap<>();
		
		
		for(UserAssetDTO asset:userStocksWithUser) {
			String stockCode = asset.getDetailCode();
			
			Long shares =  Long.parseLong(asset.getQuantity());     
			Long price = Long.parseLong(asset.getPurchasePrice());
			
			if(totalSharesByStockCode.containsKey(stockCode)) {
				Long prevShares = totalSharesByStockCode.get(stockCode);
				Long prevAmount = totalAmountByStockCode.get(stockCode);
				
				totalSharesByStockCode.put(stockCode, prevShares+shares);
				totalAmountByStockCode.put(stockCode, prevAmount+(shares*price));
				
			} else {
				totalSharesByStockCode.put(stockCode, shares);
				totalAmountByStockCode.put(stockCode, price*shares);
			}
			
		}
		
		
		for(String stockCode:totalSharesByStockCode.keySet()) {
			
			Optional<StockTableEntity> stockTable = stockRepo.findById(stockCode);
			String stockName = stockTable.get().getCorpname();
			
			
			long avergPrice = totalAmountByStockCode.get(stockCode)/totalSharesByStockCode.get(stockCode);
			averageStockPriceByStockName.put(stockName, avergPrice);
		}
		return averageStockPriceByStockName;
	}
}
