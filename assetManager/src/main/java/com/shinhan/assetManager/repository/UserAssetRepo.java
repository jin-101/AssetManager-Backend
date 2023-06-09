package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.user.MultikeyForUser;
import com.shinhan.assetManager.user.UserAssetDTO;

public interface UserAssetRepo extends CrudRepository<UserAssetDTO, MultikeyForUser> {
	
	@Query("select u from UserAssetDTO u where u.userId=?1 and u.assetCode =?2 and u.detailCode=?3")
	List<UserAssetDTO> getSpecificUserAssets(String userId,String assetCode,String detailCode);
	
	@Query("select u from UserAssetDTO u where u.userId=?1 and u.assetCode =?2")
	List<UserAssetDTO> getSpecificUserAssets(String userId,String assetCode);

}
