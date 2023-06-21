package com.shinhan.assetManager.controller;

import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.apt.AdministrativeDistrictGuDTO;

public interface AdministrativeDistrictGuRepo extends CrudRepository<AdministrativeDistrictGuDTO, String> {
	
	// 구 코드로부터 => 구 이름을 얻는 메소드
	public AdministrativeDistrictGuDTO findByGu(String gu);

}
