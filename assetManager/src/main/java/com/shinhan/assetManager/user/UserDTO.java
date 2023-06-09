package com.shinhan.assetManager.user;

import javax.persistence.Entity;
import javax.persistence.Id;
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
@ToString
@Entity
@Table(name = "user")
@Component // @Autowired를 위해 필요
public class UserDTO {

	@Id
	private String ssn; // 주민번호 (Social Security Number)
	private String userId; // 아이디
	private String userPw; // 패스워드
	private String userEmail; // 이메일
	private String userAddress; // 주소
	private String userName; // 이름
	private String salt; // 암호화를 위한 salt
}
