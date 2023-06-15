package com.shinhan.assetManager.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.AES256;
import com.shinhan.assetManager.user.UserDTO;

@Service
public class UserService implements AssetService {

	@Autowired
	UserRepo uRepo;
	@Autowired
	UserDTO user;
	
	@Autowired
	AES256 aes256;
	
	//사용자 기본정보 업데이트
	public String updateUserInfomation(UserDTO userDto){
		UserDTO originalUserInfo = uRepo.findById(userDto.getUserId()).get();
		
		String salt = originalUserInfo.getSalt();
		String userPw = userDto.getUserPw();
		String text = userPw+salt;
		String decryptedPw="";
		try {
			decryptedPw = aes256.encryptAES256(text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(originalUserInfo.toString());
		originalUserInfo.setUserPw(decryptedPw);
		originalUserInfo.setPhoneNumber(userDto.getPhoneNumber());
		originalUserInfo.setUserEmail(userDto.getUserEmail());
		originalUserInfo.setZipCode(userDto.getZipCode());
		originalUserInfo.setUserAddress(userDto.getUserAddress());
		originalUserInfo.setUserDetailAddress(userDto.getUserDetailAddress());
		
		uRepo.save(originalUserInfo);
		
		return "기본정보 수정을 성공하였습니다.";
	}
	
	//사용자 기본정보 조회
	public Map<String,String> searchUserInfomation(String userId){
		UserDTO u = uRepo.findById(userId).get();
		Map<String,String> userSearchInfo = new HashMap<>();
		
		String salt = u.getSalt();
		String userPw = u.getUserPw();
		String decryptedPw="";
		try {
			decryptedPw = aes256.decryptAES256(userPw);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		userSearchInfo.put("userPw",decryptedPw.replace(salt, ""));
		userSearchInfo.put("phoneNumber",u.getPhoneNumber());
		userSearchInfo.put("userEmail",u.getUserEmail());
		userSearchInfo.put("zipCode",u.getZipCode());
		userSearchInfo.put("userAddress",u.getUserAddress());
		userSearchInfo.put("userDetailAddress",u.getUserDetailAddress());
		
		return userSearchInfo;
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
