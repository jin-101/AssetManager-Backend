package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.user.UserDTO;

@Repository 
public interface UserRepo extends CrudRepository<UserDTO, String>{
	
	// 1명당 최대 3개 계정 limit 위한 메소드
	public List<UserDTO> findBySsn(String ssn);
	
	// 아이디 찾기 기능을 위한 메소드
	public List<UserDTO> findByUserNameAndPhoneNumber(String userName, String phoneNumber);
	
	//
	public List<UserDTO> findByUserName(String userName);

}
