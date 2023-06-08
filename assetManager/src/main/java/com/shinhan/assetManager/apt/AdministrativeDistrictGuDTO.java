package com.shinhan.assetManager.apt;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "district_gu")
@BatchSize(size = 100) 
public class AdministrativeDistrictGuDTO { // 한국 행정구역 DTO_2 (구 코드 / 구 이름)
	
	@Id
	private String gu; // 구 코드 (ex. 11110)
	
	private String guName; // 구 이름 (ex. 종로구) 
}
