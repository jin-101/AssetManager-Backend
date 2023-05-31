package com.shinhan.assetManager.apt;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "district")
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdministrativeDistrictDTO { // 한국 행정구역 DTO (시 / 구 / 동)
	
	@Id
	private String dong; // 동
	
	private String sido; // 시,도
	private String gu; // 구
}
