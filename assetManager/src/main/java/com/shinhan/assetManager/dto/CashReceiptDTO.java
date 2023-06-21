package com.shinhan.assetManager.dto;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "Cash_Receipt")
public class CashReceiptDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer detailCode;
	private String memberId;
	private LocalDateTime usedDate;
	private String content;
	private Integer usedCash;
}
