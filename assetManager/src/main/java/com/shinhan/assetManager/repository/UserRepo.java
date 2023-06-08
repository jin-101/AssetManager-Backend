package com.shinhan.assetManager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.user.UserDTO;

@Repository 
public interface UserRepo extends CrudRepository<UserDTO, String>{
	
	// 유저의 Id(주민번호 말고 아이디)를 통해 salt 값을 찾기 위한 메소드
	public UserDTO findByUserId(String userId);
	
	//
	public UserDTO findByUserPw(String userPw);

}
