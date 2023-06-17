package com.shinhan.assetManager.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.mapping.StockTableEntityMapping;
import com.shinhan.assetManager.repository.StockRepo;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.stock.StockInputDTO;
import com.shinhan.assetManager.stock.StockTableEntity;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;

@Service
public class StockService implements AssetService{
	
	//수정 
	private String assetCode = "C1";
	
	@Autowired
	private StockRepo stockRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserAssetRepo userAssetRepo;
	
	
	public String registerStock(StockInputDTO stockInputDTO) {
		String response = "등록완료";	
		
		try {
			
			System.out.println(stockInputDTO);
			Optional<UserDTO> user = userRepo.findById(stockInputDTO.getUserId());
		
			StockTableEntityMapping  map =  stockRepo.findByCorpname(stockInputDTO.getStockName());
			String stockCode = map.getStockcode();
			String market = map.getMarket();
			UserAssetDTO userAssetDto = new UserAssetDTO(user.get(), assetCode, stockCode, stockInputDTO.getPrice(), stockInputDTO.getBuyDay(), stockInputDTO.getShares());
			
			userAssetRepo.save(userAssetDto);
		} catch (Exception e) {
			response = "등록실패";
		}
		
		return response;
	}
	
	public String showHoldingStocks(String id) {
		
		Optional<UserDTO> user = userRepo.findById(id);
		
		List<UserAssetDTO> userStocksWithUser = userAssetRepo.getSpecificUserAssets(user.get(),assetCode);
		
		Map<String, Long> totalSharesByStockCode = new HashMap<>();
		Map<String, Long> totalAmountByStockCode = new HashMap<>();
		JSONArray averageStockPriceByStockName = new JSONArray();
		
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
		
		
		int stockId = 0;
		for(String stockCode:totalSharesByStockCode.keySet()) {
			
			JSONObject eachStock = new JSONObject();
			
			
			Optional<StockTableEntity> stockTable = stockRepo.findById(stockCode);
			String stockName = stockTable.get().getCorpname();
			String market = stockTable.get().getMarket();
			long avergPrice = totalAmountByStockCode.get(stockCode)/totalSharesByStockCode.get(stockCode);
			
			eachStock.put("id", stockId);
			eachStock.put("stockCode", stockCode);
			eachStock.put("stockName", stockName);
			eachStock.put("price", avergPrice);
			eachStock.put("market", market);
			stockId++;
			
			averageStockPriceByStockName.put(eachStock);
			
		}
		
		return averageStockPriceByStockName.toString();
	}
	
	
	
	
	
	@Override
	public String getPrice(String assetCode, String detailCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReturn() {
		// TODO Auto-generated method stub
		return null;
	}

}
