package com.shinhan.assetManager.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;
import com.shinhan.assetManager.mapping.StockTableEntityMapping;
import com.shinhan.assetManager.mongo.MongoDbFactory;
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
	
	private List<Document> kospi;
	
	private List<Document> kosdaq;
	
	
	public StockService() {
		MongoDatabase db =  MongoDbFactory.getDatabase();
		MongoCollection<Document> kospi = db.getCollection("kospi");
		MongoCollection<Document> kosdaq = db.getCollection("kosdaq");
		
		Bson sort = new Document("Day",-1L);
		
		Document kospiDoc = kospi.find().sort(sort).first();
		Document kosdaqDoc = kosdaq.find().sort(sort).first();
		
		this.kospi = kospiDoc.getList("Data", Document.class);
		this.kosdaq = kosdaqDoc.getList("Data", Document.class);
		
	}
	
	
	
	
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
			long stockPrice  = Long.parseLong(getPrice(market, stockCode));           
			long avergPrice = totalAmountByStockCode.get(stockCode)/totalSharesByStockCode.get(stockCode);
			double capitalGain = ((double)stockPrice-avergPrice)/avergPrice;
			
			eachStock.put("id", stockId);
			eachStock.put("stockCode", stockCode);
			eachStock.put("stockName", stockName);
			eachStock.put("price", avergPrice);
			eachStock.put("market", market);
			eachStock.put("stockPrice", stockPrice);
			eachStock.put("gain", Math.round(capitalGain*1000)/1000.0);
			eachStock.put("investedAmount", totalAmountByStockCode.get(stockCode));
			eachStock.put("totalShares", totalSharesByStockCode.get(stockCode));
			stockId++;
			
			averageStockPriceByStockName.put(eachStock);
			
		}
		
		
		
		return averageStockPriceByStockName.toString();
	}
	
	public String compareReturn(String id,String stockCode,String market) {
		List<UserDTO> usersWithSpecificStock = userAssetRepo.getEveryUserWithSpecificAssets(assetCode, stockCode);
		long stockPrice = Long.parseLong(getPrice(market, stockCode));
		JSONArray usersWithGain = new JSONArray();
		
		usersWithSpecificStock.forEach(user -> {
			
			 List<UserAssetDTO> userStockLogs = userAssetRepo.getSpecificUserAssets(user, assetCode, stockCode);
			 
			 long totalShares = 0L;
			 long totalAmounts =0L;
			 for(UserAssetDTO userStockLog:userStockLogs) {
				 totalShares +=  Long.parseLong(userStockLog.getQuantity());
				 totalAmounts += Long.parseLong(userStockLog.getPurchasePrice())* Long.parseLong(userStockLog.getQuantity());
			 }
			 double avergeBuyPrice = totalAmounts/totalShares;
			 double capitalGain = ((double)stockPrice-avergeBuyPrice)/avergeBuyPrice;
			 capitalGain = Math.round(capitalGain*1000)/1000.0;
			 
			 JSONObject userWithGain = new JSONObject();
			 userWithGain.put("id", user.getUserId());
			 userWithGain.put("gain", capitalGain);
			 usersWithGain.put(userWithGain);			 
			 
		});
		
		
		
		return usersWithGain.toString();
	}
	
	public String getPriceLimit() {
		List<Document> priceUpperStocks = new ArrayList<>();
		List<Document> priceLowerStocks = new ArrayList<>();
		
		for(int i=0;i<kospi.size();i++) {
			Document stock = kospi.get(i);
			String flucStr = stock.getString("flucRate");
			double flucRate = Double.parseDouble(flucStr);
			
			if(flucRate>=10.0) {
				priceUpperStocks.add(stock);
			}else if(flucRate<=-10.0) {
				priceLowerStocks.add(stock);
			}
		}
		
		for(int i=0;i<kosdaq.size();i++) {
			Document stock = kosdaq.get(i);
			String flucStr = stock.getString("flucRate");
			double flucRate = Double.parseDouble(flucStr);
			
			if(flucRate>=10.0) {
				priceUpperStocks.add(stock);
			}else if(flucRate<=-10.0) {
				priceLowerStocks.add(stock);
			}
		}
		
		
		
		
		return priceLowerStocks.toString();
	}
	
	
	
	
	@Override
	public String getPrice(String market, String detailCode) {
		
		String price = null;
	
		if(market.equals("kospi")) {
			
			for(int i=0;i<kospi.size();i++) {
				Document stock  = kospi.get(i);
				
				if(stock.get("corpCode").equals(detailCode)) {
					price = (String) stock.get("closePrice");
					
				}
				
			}
			
		} else {
			
			for(int i=0;i<kosdaq.size();i++) {
				
				Document stock  = kosdaq.get(i);
				
				if(stock.get("corpCode").equals(detailCode)) {
					price = (String) stock.get("closePrice");
					
				}
				
			}
				
		}
		
		
		return price;
	}

	@Override
	public String getReturn() {
		// TODO Auto-generated method stub
		return null;
	}

}
