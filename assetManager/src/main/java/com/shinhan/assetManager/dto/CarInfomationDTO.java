package com.shinhan.assetManager.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder 
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "carInfomation")
public class CarInfomationDTO {
	@Id
	private String infomationId;
	@ManyToOne
	private CarModelDTO carModel;
	private String type;
	private String year;
	private int price;
}
