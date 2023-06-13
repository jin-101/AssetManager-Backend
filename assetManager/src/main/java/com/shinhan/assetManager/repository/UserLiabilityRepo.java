package com.shinhan.assetManager.repository;

import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.user.MultikeyForUserLiabiltiy;
import com.shinhan.assetManager.user.UserLiabilityDTO;

public interface UserLiabilityRepo extends CrudRepository<UserLiabilityDTO, MultikeyForUserLiabiltiy> {

}
