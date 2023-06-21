package com.shinhan.assetManager.dto;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
	private Integer detailCode;
	private String memberId; //내일 동열이한테 테이블 연결 물어보기
	private String accountNumber;
	private String bank;
	private LocalDateTime exchangeDate;
	private Integer withdraw;
	private Integer deposit;
	private String content;
	private Integer balance;
	private String memo;
	private String category;
	
//	@ManyToOne
//	HouseholdAccountsCategoryDTO category;
}
