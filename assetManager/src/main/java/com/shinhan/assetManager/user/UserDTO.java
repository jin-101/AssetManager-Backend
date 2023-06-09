package com.shinhan.assetManager.user;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
	
	@OneToMany 
	//@JoinColumn(name = "test")
	List<UserAssetDTO> userAssets; 
}
