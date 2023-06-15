package com.shinhan.assetManager.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.mapping.StockTableEntityMapping;
import com.shinhan.assetManager.repository.StockRepo;
import com.shinhan.assetManager.repository.UserAssetRepo;
import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.stock.StockInputDTO;
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
