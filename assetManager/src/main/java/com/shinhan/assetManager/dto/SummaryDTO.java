package com.shinhan.assetManager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SummaryDTO {
	private int withdraw; // 월별 지출액
    private int deposit; // 월별 수입액
}
