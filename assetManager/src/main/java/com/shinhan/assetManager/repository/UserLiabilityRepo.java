package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.user.MultikeyForUserLiability;
import com.shinhan.assetManager.user.UserDTO;
import com.shinhan.assetManager.user.UserLiabilityDTO;

@Repository
public interface UserLiabilityRepo extends CrudRepository<UserLiabilityDTO, MultikeyForUserLiability> {

	// 거주주택마련부채 관련 메소드
	public List<UserLiabilityDTO> findByUserAndLiabilityCode(UserDTO user, String liabilityCode);
	
	// 총부채 얻는 메소드
	public List<UserLiabilityDTO> findByUser(UserDTO user); 
}
