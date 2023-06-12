package com.shinhan.assetManager.jwt;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shinhan.assetManager.repository.UserRepo;
import com.shinhan.assetManager.user.UserDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

// main 함수가 있는 클래스 
@Component
public class JavaJwt {
	
	@Autowired
	UserRepo userRepo;

	// 암호화하기 위한 보안키
	private static String SECRET_KEY = "secret"; 
	// JWT 만료 시간 1시간
	private static long tokenValidMilisecond = 1000L * 60 * 60; // (일단 60분으로 책정하고 테스트)
	static JavaJwt p = new JavaJwt();
	
	// ★★★ 0. 공통 메소드 : JWT(토큰)로부터 userId 얻는 메소드
	public UserDTO getUserIdFromJwt(HttpServletRequest request) {
		// JWT(토큰)로부터 현재 로그인한 유저 ID 얻기 (보안상 Front <-> Back 사이에서 ID를 주고 받는 게 아니라 JWT를 주고 받게끔 설정)
		String bearerToken = request.getHeader("Authorization"); // ★ axios header 부분에 Authorization: `${id}` 를 추가해줘야 함
		JavaJwt jwt = new JavaJwt(); 
		String userId = jwt.verifyAndDecodeToken(bearerToken);
		System.out.println("로그인한 유저 ID : " + userId);
		
		// ★ 토큰 만료시 => java.util.NoSuchElementException: No value present 예외 뜸
		UserDTO user = userRepo.findById(userId).get();

		return user;
	}

	// 적용해야 하는 메소드 1. 토큰 생성 함수
	public String createToken(String key) {
		// Claims을 생성
		Claims claims = Jwts.claims().setId(key);
		// Payload 데이터 추가
		claims.put("test", "Hello world");
		// 현재 시간
		Date now = new Date();
		// JWT 토큰을 만드는데, Payload 정보와 생성시간, 만료시간, 알고리즘 종류와 암호화 키를 넣어 암호화 함.
		String token = Jwts.builder().setHeaderParam("alg", "HS256") // 암호화 알고리즘 설정
				.setHeaderParam("typ", "JWT") // 토큰 타입 설정
				.setClaims(claims).setIssuedAt(now).setExpiration(new Date(now.getTime() + tokenValidMilisecond))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 서명에 사용할 알고리즘과 비밀 키 설정
				.compact(); // JWT 생성

		System.out.println("생성된 token : " + token);

		return token;
	}

	// 적용해야 하는 메소드 2. 토큰 검증 및 복호화 파트
	public String verifyAndDecodeToken(String token) {
		String id = "";
		// JWT 토큰 복호화
		Jws<Claims> claims = p.getClaims(token);
		// JWT 토큰 검증
		if (claims != null && p.validateToken(claims)) {
			// id를 취득한다.
			id = p.getKey(claims);
			// Payload 값의 test 키의 값을 취득
			Object test = p.getClaims(claims, "test");
			// 콘솔 출력
			System.out.println("getKey : " + id);
			System.out.println("getClaims : " + test);
		} else {
			// 토큰이 정합성이 맞지 않으면
			System.out.println("error");
		}
		return id;
	}

	// 2-1. String으로 된 코드를 복호화한다.
	public Jws<Claims> getClaims(String jwt) {
		try {
			// 암호화 키로 복호화한다.
			// 즉 암호화 키가 다르면 에러가 발생한다.
			Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt);
			System.out.println("Jws<Claims> : "+claims);
			Claims claims2 = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
			System.out.println("claims : "+claims2);
			return claims;
		} catch (ExpiredJwtException e) { // ★ 토큰이 만료되었을 경우
            e.printStackTrace();
            return null; // ★ 뭘 리턴해줘야 할까..?
        } catch (SignatureException e) {
			return null;
		}
	}

	// 2-2 ~ 2-4 : ★ 파라미터 타입을 Jws<Claims>가 아니라 Claims로 바꿔줘볼까?
	// 2-2. 토큰 검증 함수
	public boolean validateToken(Jws<Claims> claims) {
		// 토큰 만료 시간이 현재 시간을 지났는지 검증
		Boolean bool = !claims.getBody().getExpiration().before(new Date());
		return bool;
	}

	// 2-3. 토큰을 통해 Payload의 ID를 취득
	public String getKey(Jws<Claims> claims) {
		// Id 취득
		String id = claims.getBody().getId();
		return id;
	}

	// 2-4. 토큰을 통해 Payload의 데이터를 취득
	public Object getClaims(Jws<Claims> claims, String key) {
		// 데이터 취득
		Object obj = claims.getBody().get(key);
		return obj;
	}
}