package com.shinhan.assetManager.deposit;

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
@Table(name = "user_deposit")
public class DepositSavingsDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long detailCode;
	private String depositType;
	private String bank;
	private String productName;
	private String startDate;
	private String endDate;
	private String price;
	private String rate;
}
