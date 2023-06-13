package com.shinhan.assetManager.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
@ToString//(exclude = "")
@Entity
@Table(name = "user_asset")  
@Component
@IdClass(MultikeyForUser.class) // 복합키 설정을 위한 @ (설명 ppt 100)
public class UserAssetDTO { // 개인 자산 테이블
	
	@Id
	@ManyToOne // ★ 1. 양방향 연관관계를 위해 추가 (근데 굳이 양방향은 안해도 됐을 거 같은데..)
	@JoinColumn(name = "user_id") // ★ 2. 부모테이블의 Id와 서로 name 맞춰주기
	private UserDTO user; // ★ 3. 부모 엔티티를 사용!
	@Id 
	@Column(length = 50, name = "asset_code") // 따로 이름 지정해준 이유 : 멀티키라서 이름이 엄청 길어지더라고
	private String assetCode; // 자산코드
	@Id
	@Column(length = 50, name = "detail_code")
	private String detailCode; // 세부코드
	@Id
	@Column(length = 50, name = "purchase_price")
	private String purchasePrice; // 매수가
	@Id
	@Column(length = 50, name = "purchase_date")
	private String purchaseDate; // 매수일 
	  
	@Column(length = 50, name = "quantity") // @ColumnDefault 따로 지정 안해주면 디폴트값 null로 지정됨 (숫자 1은 안되는 듯? "1"만 되고)
	private String quantity; // 수량 
	
}
