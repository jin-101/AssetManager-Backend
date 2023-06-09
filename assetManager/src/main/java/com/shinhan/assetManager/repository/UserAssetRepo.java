package com.shinhan.assetManager.repository;

import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.user.MultikeyForUser;
import com.shinhan.assetManager.user.UserAssetDTO;

public interface UserAssetRepo extends CrudRepository<UserAssetDTO, MultikeyForUser> {

}
