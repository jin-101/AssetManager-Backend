package com.shinhan.assetManager.car;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder 
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_car")
public class CarDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long detailCode;
	private String company;
	private String model;
	private String year;
	private String price;
	
}
