package com.shinhan.assetManager.realAssets;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "realassets")
public class RealAssetDTO {
	
	@Id
	private LocalDate day;
	private double usd;
	private double eur;
	private double gbp;
	private double cnh;
	private double jpy;
	private double gold99k;
	private double minigold100g;
	

}
