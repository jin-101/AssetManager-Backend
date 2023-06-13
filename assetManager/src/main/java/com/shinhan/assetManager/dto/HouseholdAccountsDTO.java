package com.shinhan.assetManager.dto;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Household_Accounts")
public class HouseholdAccountsDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String detailCode;
	private String memberId;
	private String accountNumber;
	private String bank;
	private Date exchangeDate;
	private String exchangeTime;
	private Integer withdraw;
	private Integer deposit;
	private String content;
	private Integer balance;
	private String memo;
	private String category;
	
//	@ManyToOne
//	HouseholdAccountsCategoryDTO category;
}
