package com.shinhan.assetManager.repository;

import org.springframework.data.repository.CrudRepository;

import com.shinhan.assetManager.car.CarDTO;

public interface CarDTOrepo extends CrudRepository<CarDTO, Long> {

}
