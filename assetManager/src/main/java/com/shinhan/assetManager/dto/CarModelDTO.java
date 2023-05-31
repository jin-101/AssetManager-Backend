package com.shinhan.assetManager.dto;

import lombok.Builder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder 
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "carModel")
public class CarModelDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)//oracle:sequence, mysql:identity
	private Integer modelId;
	@NonNull 
	private String className;
	@NonNull 
	private String carName;
	@NonNull 
	private String modelName;
	private String gradeName;
	
	@ManyToOne
	private CarCompanyDTO carCompany;
}
