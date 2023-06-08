package com.shinhan.assetManager.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.stereotype.Component;

import com.shinhan.assetManager.apt.MultikeyForDistrict;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user_asset") 
@Component
@IdClass(MultikeyForUser.class) // 복합키 설정을 위한 @ (설명 ppt 100)
public class UserAssetDTO { // 개인 자산 테이블
	
	@Id
	@Column(length = 50)
	private String userId;
	@Id
	@Column(length = 50)
	private String assetCode; // 자산코드
	@Id
	@Column(length = 50)
	private String detailCode; // 세부코드
	@Id
	@Column(length = 50)
	private String purchasePrice; // 매수가
	@Id
	@Column(length = 50)
	private String purchaseDate; // 매수일 
	  
	@Column(length = 50) // @ColumnDefault 따로 지정 안해주면 디폴트값 null로 지정됨 (숫자 1은 안되는 듯? "1"만 되고)
	private String quantity; // 수량 
	
//	@OneToMany 
//	@JoinColumn(name = "user_id")
//	private List<UserDTO> user;

}
