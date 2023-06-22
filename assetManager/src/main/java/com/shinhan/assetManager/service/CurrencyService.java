package com.shinhan.assetManager.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.realAssets.CurrencyInputDTO;
import com.shinhan.assetManager.realAssets.RealAssetDTO;
import com.shinhan.assetManager.repository.RealAssetsRepo;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;

@Service
public class CurrencyService implements AssetService {
	
	private String assetCode = "A2";
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserAssetRepo userAssetRepo;
	
	@Autowired
	private RealAssetsRepo realAssetRepo;
	
	
	public String registerCurrency(CurrencyInputDTO currencyInputDTO) {
		String response = "등록완료";	
		
		try {
			
			System.out.println(currencyInputDTO);
			Optional<UserDTO> user = userRepo.findById(currencyInputDTO.getUserId());
			UserAssetDTO userAssetDto = new UserAssetDTO(user.get(), assetCode, 
					currencyInputDTO.getCurrency(), currencyInputDTO.getPrice(), 
					currencyInputDTO.getBuyDay(), currencyInputDTO.getShares());
			
			userAssetRepo.save(userAssetDto);
		} catch (Exception e) {
			response = "등록실패";
		}
			
		return response;
		
	}
	
	public String showHoldingCurrency(String id) {
		Optional<UserDTO> user = userRepo.findById(id);
		
		List<UserAssetDTO> userCurrenciesWithUser = userAssetRepo.getSpecificUserAssets(user.get(),assetCode);
		Map<String, Double> totalAmountByCurrency = new HashMap<>();
		Map<String, Double> totalSharesByCurrency = new HashMap<>();
		JSONArray buyCurrency = new JSONArray();
		
		for(UserAssetDTO asset:userCurrenciesWithUser) {
			
			String currency =  asset.getDetailCode();
			double buyPrice = Double.parseDouble(asset.getPurchasePrice());
			double buyQuantity = Double.parseDouble(asset.getQuantity());
			
			if(currency.equals("jpy")) {
				buyPrice = buyPrice/100.0;
			}
			
			
			if(totalAmountByCurrency.containsKey(currency)) {
				double prevAmount = totalAmountByCurrency.get(currency);
				double prevQuantity = totalSharesByCurrency.get(currency);
				
				double amount = prevAmount + (buyPrice*buyQuantity);
				double quantity  =  prevQuantity + buyQuantity;
				
				totalAmountByCurrency.put(currency, amount);
				totalSharesByCurrency.put(currency, quantity);
				
				
			} else {
				totalAmountByCurrency.put(currency, buyPrice*buyQuantity);
				totalSharesByCurrency.put(currency, buyQuantity);
			}			
			
		}
		
		
		for(String currency:totalAmountByCurrency.keySet()) {
			
			double averagePrice = 0.0d;
			double marketPrice  = 0.0d;
			double gain = 0.0d;
			
			if(currency.equals("jpy")) {
				double priceBeforeRound = (totalAmountByCurrency.get(currency)/totalSharesByCurrency.get(currency))*100;
				averagePrice = Math.round(priceBeforeRound*1000)/1000.0;
				marketPrice = Double.parseDouble(getPrice(assetCode, currency));
				gain = (marketPrice-averagePrice)/averagePrice;
				gain = Math.round(gain*1000)/1000.0;
				
				
			} else {
				double priceBeforeRound = (totalAmountByCurrency.get(currency)/totalSharesByCurrency.get(currency));
				averagePrice = Math.round(priceBeforeRound*1000)/1000.0;
				marketPrice = Double.parseDouble(getPrice(assetCode, currency));
				gain = (marketPrice-averagePrice)/averagePrice;
				gain = Math.round(gain*1000)/1000.0;

			}
			
			JSONObject eachCurrency = new JSONObject();
			eachCurrency.put("currency", currency);
			eachCurrency.put("buyPrice", averagePrice);
			eachCurrency.put("marketPrice", marketPrice);
			eachCurrency.put("gain", gain);
			eachCurrency.put("investedAmount",totalAmountByCurrency.get(currency));
			eachCurrency.put("totalShares", totalSharesByCurrency.get(currency));
			
			buyCurrency.put(eachCurrency);
			
			
		}
		
		return buyCurrency.toString();
	}
	
	public String getReadyGraph() {
		Iterable<RealAssetDTO>  realAssetDtos = realAssetRepo.findAll();
		List<Double> cnhList = new ArrayList<>();
		List<Double> response = new ArrayList<>();
		
		realAssetDtos.forEach(realAsset ->cnhList.add(realAsset.getCnh()));
		
		for(int i=0;i<cnhList.size();i++) {
			response.add(cnhList.get(i));
		}
		
		
		
		return response.toString();
	}
	
	

	@Override
	public String getPrice(String market, String detailCode) {
		Optional<RealAssetDTO> realAssetOp  = realAssetRepo.findById(LocalDate.of(2023, 6, 9));
		RealAssetDTO realAsset = realAssetOp.get();
		
		String currenctPrice = null;
		
		
		switch (detailCode) {
		case "usd":
			currenctPrice = String.valueOf(realAsset.getUsd());
			break;
		case "eur":
			currenctPrice = String.valueOf(realAsset.getEur());
			break;
		case "jpy":
			currenctPrice = String.valueOf(realAsset.getJpy());
			break;
		case "gbp":
			currenctPrice = String.valueOf(realAsset.getGbp());
			break;
		case "cnh":
			currenctPrice = String.valueOf(realAsset.getCnh());
			break;
		default:
			break;
		}
		
		return currenctPrice;
	}

	@Override
	public String getReturn() {
		// TODO Auto-generated method stub
		return null;
	}

}
