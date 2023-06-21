package com.shinhan.assetManager.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "assetDetail")
@Table(name = "user_liability")
@Entity
@Component 
@IdClass(MultikeyForUserLiability.class)
public class UserLiabilityDTO { // 개인 부채 테이블

	@Id
	@ManyToOne
	@JoinColumn(name = "user_id") 
	private UserDTO user; // 1. 회원 아이디
	
	@Id  
	@Column(length = 50, name = "liability_code")
	private String liabilityCode; // 2. 부채코드 (ex. S1, L1 ...)
	
	@Id
	@Column(length = 50, name = "detail_code")
	private String detailCode; // 3. 세부코드 (ex. 대출이름 - 주담대, 신용대출 등)
	
	@Column(length = 50, name = "loan_amount")
	private String loanAmount; // 4. 대출금액
	
	@Column(length = 50, name = "rate")
	private String rate; // 5. 대출금리
	
	@Column(length = 50, name = "maturity_date")
	private String maturityDate; // 6. 대출만기
	
	// ★ 굳이 UserAssetDTO와 연관관계를 맺진 않아도 될 듯..? => (정정) 하는게 맞는 거 같음. 부채 데이터 넣을 때마다 detail_code를 일일이 얻는게 말이 안되지 않나?
	@ManyToOne
	private UserAssetDTO assetDetail; // 7. 자산_세부코드 (asset_detail_code)
	 
}
