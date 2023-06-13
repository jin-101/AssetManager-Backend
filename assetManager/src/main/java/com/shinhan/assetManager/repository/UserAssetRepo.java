package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.user.MultikeyForUserAsset;
import com.shinhan.assetManager.user.UserAssetDTO;
import com.shinhan.assetManager.user.UserDTO;

public interface UserAssetRepo extends CrudRepository<UserAssetDTO, MultikeyForUserAsset> { 
	
	@Query("select u from UserAssetDTO u where u.user=?1 and u.assetCode =?2 and u.detailCode=?3")
	List<UserAssetDTO> getSpecificUserAssets(UserDTO user,String assetCode,String detailCode);
	
	@Query("select u from UserAssetDTO u where u.user=?1 and u.assetCode =?2") 
	List<UserAssetDTO> getSpecificUserAssets(UserDTO user,String assetCode);   

}
