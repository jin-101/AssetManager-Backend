package com.shinhan.assetManager.dto;

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
@Table(name = "Year_End_Tax")
public class YearEndTaxDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer detailCode; //PK
	private String memberId; //회원 아이디
	
	private Integer salary; //연봉
	private Integer nonTaxIncome; //비과세 소득
	
	private Integer spouse; //배우자 1이면 배우자 있음, 0이면 배우자 없음
	private Integer children; //자녀수 (직계존속) 
	private Integer parents; //부모수 (직계비속)
	private Integer sibling; //형제자매
	private Integer fosterChildren; //위탁아동
	private Integer lowIncomePeople; //기초수급자
	
	private Integer oldPeople; //경로우대자 
	private Integer disabledPerson; //장애인
	private Integer woman; //부녀자
	private Integer oneParent; //한부모 1이면 한부모, 0이면 한부모 아님
	private Integer newBirthChild; //이번 해에 출생 or 입양한,  자녀 1이면 첫째, 2면 둘째, 3이면 셋째 이상(진형한테 새로 추가된거 말해주자)
	
	private Integer loanRepaymentPrincipal; //대출상환원금 ~원 입력받기 (용희형거에서도 입력받고 연말정산에서도 입력받기로 함)
	private Integer loanRepaymentInterest; //대출상환이자  ~원 입력받기 (용희형거에서도 입력받고 연말정산에서도 입력받기로 함)
	
	private Integer ginapbuseaek; //기납부세액 ~원 입력받기
	
	
}
