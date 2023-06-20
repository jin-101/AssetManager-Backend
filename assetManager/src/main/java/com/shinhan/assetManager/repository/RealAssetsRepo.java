package com.shinhan.assetManager.repository;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.realAssets.RealAssetDTO;

public interface RealAssetsRepo extends CrudRepository<RealAssetDTO, LocalDate>{

}
