package com.shinhan.assetManager.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder 
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carCompany")
public class CarCompanyDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)//oracle:sequence, mysql:identity
	private Integer companyId;
	@NonNull
	private String companyName;
}
