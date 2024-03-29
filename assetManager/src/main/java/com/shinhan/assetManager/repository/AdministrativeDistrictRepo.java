package com.shinhan.assetManager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shinhan.assetManager.apt.AdministrativeDistrictDTO;
import com.shinhan.assetManager.apt.MultikeyForDistrict;

@Repository
public interface AdministrativeDistrictRepo extends CrudRepository<AdministrativeDistrictDTO, MultikeyForDistrict> { // 복합키의 경우 : key에 String이 아니라 복합키 클래스를 줘야 한다?! (by 동열) 
	
	// # 아파트 입력을 위한 메소드
	// 1. 서울특별시(11)를 고르면 => 종로구~강동구까지 보이게
	public List<AdministrativeDistrictDTO> findBySido(String sido);
	
	// 2. 종로구(11110)를 고르면 => 청운동~숭인2동이 보이게
	public List<AdministrativeDistrictDTO> findByGu(String gu);
	
	// 3. 청운동을 고르면 => 그 동의 아파트 리스트를 검색할 수 있게
	public List<AdministrativeDistrictDTO> findByDong(String dong);
}
