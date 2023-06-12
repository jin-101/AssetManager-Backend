package com.shinhan.assetManager.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	//수정 
	private String assetCode = "C1";
	private UserDTO user = new UserDTO();
	
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
	
	@GetMapping("/stockPrice")
	@ResponseBody
	public List<UserAssetDTO> handleStockPriceRequest() {
		List<UserAssetDTO> userStocks = userAssetRepo.getSpecificUserAssets(user, assetCode);
		
		Map<String, Long> totalSharesByStockCode = new HashMap<>();
		Map<String, Long> totalAmountByStockCode = new HashMap<>();
		
		
		for(UserAssetDTO asset:userStocks) {
			String stockCode = asset.getDetailCode();
			Long shares =  Long.parseLong(asset.getQuantity());     
			Long price = Long.parseLong(asset.getPurchasePrice());
			Long avergePrice = 0L;
			
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
			System.out.println(totalAmountByStockCode.get(stockCode)/totalSharesByStockCode.get(stockCode));
			System.out.println("----------------------------------");
		}
		return userStocks;
	}
}
