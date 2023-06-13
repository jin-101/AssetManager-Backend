package com.shinhan.assetManager.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
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
@ToString(exclude = "userAssets") // 
@Entity
@Table(name = "user") 
@Component // @Autowired를 위해 필요 
public class UserDTO {

	@Id
	@Column(name = "user_id")
	private String userId; // 아이디
	 
	//@NotNull // 인용한 글에서는, 결론적으로 nullable = false 보다 @NotNull을 추천하고 있다. (참조: https://kafcamus.tistory.com/15) 
	private String userPw;
	private String userName; 
	private String ssn; // 주민번호 (Social Security Number)
	private String userEmail; 
	private String phoneNumber;  
	private String userAddress;    
	private String salt; // 암호화를 위한 salt
	
	// 로그인 시도 횟수 제한 관련 field 
	@ColumnDefault("0") // 참조: https://velog.io/@minji/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-%EC%97%94%ED%8B%B0%ED%8B%B0-%EC%BB%AC%EB%9F%BC-default-value-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0-ColumnDefault-Builder.Default-%EC%B0%A8%EC%9D%B4
	private int loginFailCount;
	@Column(columnDefinition = "char(1) default 'N'", nullable = false) // 참조: https://squirmm.tistory.com/entry/Spring-Jpa-Default-%EC%84%A4%EC%A0%95
	private String accountLockStatus;  
	private Long latestLoginDate; 
	
	@OneToMany 
	@JoinColumn(name = "user_id")
	List<UserAssetDTO> userAssets; 
	
	@OneToMany 
	@JoinColumn(name = "user_id")
	List<UserLiabilityDTO> userLiabilities; 
}
