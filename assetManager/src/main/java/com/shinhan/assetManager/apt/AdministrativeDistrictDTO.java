package com.shinhan.assetManager.apt;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

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
@Table(name = "district")
@IdClass(DistrictMultiKey.class) // 복합키 설정을 위한 @ (설명 ppt 100)
public class AdministrativeDistrictDTO { // 한국 행정구역 DTO (시 / 구 / 동)
	
	@Id
	private String dong; // 동
	@Id
	private String gu; // 구
	
	private String sido; // 시,도 
}
