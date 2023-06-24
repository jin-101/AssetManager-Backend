package com.shinhan.assetManager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shinhan.assetManager.dto.CashReceiptDTO;
import com.shinhan.assetManager.dto.YearEndTaxDTO;
import com.shinhan.assetManager.repository.CashReceiptRepository;
import com.shinhan.assetManager.repository.HouseholdAccountsRepository;
import com.shinhan.assetManager.repository.YearEndTaxRepository;

@RestController
@RequestMapping("/rest/yearEndTax")
public class YearEndTaxController {
	
	@Autowired
	CashReceiptRepository cashRepo;
	
	@Autowired
	YearEndTaxRepository TaxRepo;
	
	@Autowired
	HouseholdAccountsRepository accRepo;
	
	//연말정산을 위해 정보 입력받은 것 저장하기 
	@PostMapping(value = "/saveTaxInformation.do", consumes = "application/json")
	public void saveTaxInformation(@RequestBody List<YearEndTaxDTO> dto) {		 
		TaxRepo.saveAll(dto);
	}
	
	//일단 진짜 대강 한글로 막 쳐서 조건문이라도 적어두자...
	//계산해서 환급/납부액 계산하는 컨트롤러
	@PostMapping(value = "/calculateTax.do", consumes = "application/json")
	public int calculateTax() {
		List<YearEndTaxDTO> informationForTaxList = TaxRepo.findByMemberId("jin"); //해당 아이디의 연말정산을 위해 입력받은 정보 리스트
		System.out.println("연말정산 정보(from 연말정산 테이블) : " + informationForTaxList);
		
		int sumCashReceipt = cashRepo.sumCashReceipt("jin", 2023); //2023년 현금영수증 총액
		System.out.println("현금영수증 총액 : " + sumCashReceipt);
		
		int sumCardReceipt = accRepo.sumYearWithdraw("jin", 2023); //2023년 총 지출액 from 가계부
		System.out.println("총 지출액 :" + sumCardReceipt);
		
		int yeonbong  =  informationForTaxList.get(0).getSalary(); //연봉(총급여액)
		System.out.println("연봉(총급여액) : " + yeonbong);
		
		int bigwasesodeuk = informationForTaxList.get(0).getNonTaxIncome(); //비과세소득은 매달 달라서 사용자도 예측하기 어려움
		System.out.println("비과세 소득 : " + bigwasesodeuk);                 //그래서 1년 예상치 비과세 소득을 입력해 달라 해야됨
		
		int spouse = informationForTaxList.get(0).getSpouse();
		System.out.println("배우자 여부 : " + spouse);
		
		int children = informationForTaxList.get(0).getChildren(); //만 20세 이하
		System.out.println("직계비속(자녀수) : " + children);
		
		int parents = informationForTaxList.get(0).getParents();
		System.out.println("직계존속(부모) : " + parents);
		
		int sibling = informationForTaxList.get(0).getSibling();
		System.out.println("형재/자매 : " + sibling);
		
		int fosterchildren = informationForTaxList.get(0).getFosterChildren();
		System.out.println("위탁아동 : " + fosterchildren);
		
		int lowincomepeople = informationForTaxList.get(0).getLowIncomePeople();
		System.out.println("기초수급자 : " + lowincomepeople);
		
		int chulsanibyangjanyeo  = informationForTaxList.get(0).getNewBirthChild();
		System.out.println("출산/입양 아동 (첫째, 둘째, 셋쩨 이런거): "  + chulsanibyangjanyeo);
		
		int sumGibongongjeCount = spouse + children + parents + sibling + fosterchildren + lowincomepeople;
		System.out.println("기본공제 명수 합친 것 : " + sumGibongongjeCount);
		
		//추가공제 항목들
		//경로우대자
		int oldPeople = informationForTaxList.get(0).getOldPeople();
		
		//장애인
		int disableperson = informationForTaxList.get(0).getDisabledPerson();
		
		//부녀자
		int woman = informationForTaxList.get(0).getWoman();
		
		//한부모 여부
		int oneparent = informationForTaxList.get(0).getOneParent();
		
		
		int geunrosodeukgongje  = 0; //근로소득 공제
				//인트로 강제형변환 하면 소수점 밑의 자리는 버려짐
				if (yeonbong <= 5000000 ) {
					geunrosodeukgongje = (int) (yeonbong * 0.7);
				} else if (yeonbong <= 15000000 ) {
					geunrosodeukgongje = (int) (3500000 + (yeonbong - 5000000) * 0.4);
				} else if (yeonbong <= 45000000) {
					geunrosodeukgongje = (int) (7500000 + (yeonbong - 15000000) * 0.15);
				} else if (yeonbong <= 100000000) {
					geunrosodeukgongje = (int) (12000000 + (yeonbong - 45000000) * 0.05);
				} else if (yeonbong > 100000000) {
					geunrosodeukgongje = (int) (14750000 + (yeonbong - 100000000) * 0.02);
				}
		System.out.println("근로소득 공제 : " + geunrosodeukgongje);		
				
		//근로소득금액
		//근로소득금액 = 총급여액(연봉) - 비과세근로소득 - 근로소득공제
		int geunrosodeukgeumaek  = yeonbong - bigwasesodeuk - geunrosodeukgongje;
		System.out.println("근로소득 금액 : " + geunrosodeukgeumaek);

		///////////////////////////////////1단계 끝////////////////////////////////////////////
		
		//과세표준 = 근로소득금액 - 종합소득공제 - 조특법상 소득공제
		
		//기본공제
		int gibongongje = 1500000 * (sumGibongongjeCount + 1);
		System.out.println("기본공제 : " + gibongongje);
		
		//추가공제
		int chugagongje = 1000000 * (oldPeople) 
				  		  + 2000000 * (disableperson) 
				  		  + 500000 * (woman) 
				  		  + 1000000 * (oneparent);
		System.out.println("추가공제 : " + chugagongje);
		
		//인적공제 = 기본공제 + 추가공제
		int injeokgongje = gibongongje + chugagongje;
		System.out.println("인적공제 : " + injeokgongje);
		
		//국민연금보혐료 (1년치)
		int gukminyeongeum = (int) ((yeonbong - bigwasesodeuk) * 0.045); //1년치 비과세소득 
		System.out.println("국민연금 보험료 : " + gukminyeongeum);
		
		//건강보험료
		int geongangboheom = (int) ((yeonbong - bigwasesodeuk) * 0.03545);
		System.out.println("건강보험료 : " + geongangboheom);
		
		//장기요양보험
		int janggiyoyang = (int) (geongangboheom * 0.1281);
		System.out.println("장기요양보험 : " + janggiyoyang);
		
		//주택자금
		//원리금 loanRepaymentPrincipal
		int wonrigeum = (int) (informationForTaxList.get(0).getLoanRepaymentPrincipal() * 0.4); //용희형이 준 원리금 상환액(연말정산 테이블에 따로 칼럼 만듦)
		System.out.println("주택상환 원리금 : " + wonrigeum);
		
		//이자비용 loanRepaymentInterest
		int ija =  (int) (informationForTaxList.get(0).getLoanRepaymentInterest()); //용희형이 준 이자상환액(연말정산 테이블에 따로 칼럼 만듦); 
		System.out.println("주택상환 이자비용 : " + ija);
		
		//주택마련 저축 공제 (생략 주택청약 금액을 입력하는데가 없음)
		//공제한도: 저축불입액 * 40%
		//한도액: 2,500만 원
		
		//////조특법상 소득 공제	
				
		//현금영수증 + 체크카드
		int joteukbeopsangsodeukgongje = (int) ((sumCashReceipt + sumCardReceipt) * 0.3);
		System.out.println("조특법상 소득 공제 : " + joteukbeopsangsodeukgongje);
		
		//신용카드(신용카드는 일단 생략, 체크카드, 신용카드 구분이 없음)
		//신용카드 사용액 * 0.15
		
		//조특법상 소득공제 한도
		if (yeonbong <= 70000000) {
			if(joteukbeopsangsodeukgongje > Math.min(yeonbong * 0.2, 3300000)) {
				joteukbeopsangsodeukgongje = (int) Math.min(yeonbong * 0.2, 3300000);
			}			  
		} else if (yeonbong <= 120000000) {
			joteukbeopsangsodeukgongje = 2800000;
		} else if (yeonbong > 120000000 ) {
			joteukbeopsangsodeukgongje = 2300000;
		}
		System.out.println("if문을 거친 조특법상 소득공제 : " + joteukbeopsangsodeukgongje);
		
		//과세표준 = 근로소득금액 - 종합소득공제 - 조특법상 소득공제
		int gwasepyojun =
				geunrosodeukgeumaek - (injeokgongje + gukminyeongeum + geongangboheom + janggiyoyang + wonrigeum + ija)
									- joteukbeopsangsodeukgongje;
		System.out.println("과세표준 : " + gwasepyojun);
//		/////////////////////////////////////2단계 끝//////////////////////////////////////
		
		//산출세액 = 과세표준 * 세율
		int sanchulseaek = 0;
		if (gwasepyojun <= 12000000) {
			sanchulseaek = (int) (gwasepyojun * 0.06);
		} else if (gwasepyojun <= 46000000) {
			sanchulseaek = (int) (gwasepyojun * 0.15 - 1080000);
		} else if (gwasepyojun <= 88000000) {
			sanchulseaek = (int) (gwasepyojun * 0.24 - 5220000);
		} else if (gwasepyojun <= 150000000) {
			sanchulseaek = (int) (gwasepyojun * 0.35 - 14900000);
		} else if (gwasepyojun <= 300000000) {
			sanchulseaek = (int) (gwasepyojun * 0.38 - 19400000);
		} else if (gwasepyojun <= 500000000) {
			sanchulseaek = (int) (gwasepyojun * 0.40 - 25400000);
		} else if (gwasepyojun <= 1000000000) {
			sanchulseaek = (int) (gwasepyojun * 0.42 - 35400000);
		} else if (gwasepyojun > 1000000000) {
			sanchulseaek = (int) (gwasepyojun * 0.45 - 65400000);
		}
		System.out.println("산출세액 : " + sanchulseaek);
		///////////////////////////////////3단계 끝////////////////////////////////////////
		
		//결정세액 = 산출세액 - 세금감면 - 세액공제
		int geunrosodeukseaekgongje = 0; //근로소득세액 공제 (이게 세액공제임!!!)
		if (sanchulseaek <= 1300000) {
			geunrosodeukseaekgongje = (int) (sanchulseaek * 0.55);
		} else if (sanchulseaek > 1300000) {
			geunrosodeukseaekgongje = (int) (715000 + (1300000 - sanchulseaek) * 0.30);
		}
		System.out.println("근로소득세액공제 : " + geunrosodeukseaekgongje);
	
		//가계부에서 2023년, 아이디 일치하는 리스트를 다 뽑아오되, 거기에서 카테고리가 교육/학습, 의료, 보험, 기부금인 것의 withdraw만 다 더해야 한다
		//이 네개에 대한 한도가 있지만 기준이 두개나 있고 애매하여 일단 생략하였음
		int insuranceSeaekgongje = (int) (accRepo.sumCategoryWithdraw("jin", "보험", 2023) * 0.12); //개인 보험료 지출
		int medicalSeaekgongje = (int) (accRepo.sumCategoryWithdraw("jin", "의료/건강", 2023) * 0.15); //의료비 지출
		int educationSeaekgongje = (int) (accRepo.sumCategoryWithdraw("jin", "교육/학습", 2023) * 0.15); //23년 교육비 지출
		int donationSeaekgongje = (int) (accRepo.sumCategoryWithdraw("jin", "기부금", 2023) * 0.15); // 기부금 지출
		
		System.out.println("보험료 : " + insuranceSeaekgongje);
		System.out.println("의료비 : " + medicalSeaekgongje);
		System.out.println("교육비 : " + educationSeaekgongje);
		System.out.println("기부금 : " + donationSeaekgongje);
	
//		//보험료 등 세액공제액의 한도 
//		int handoaek = sanchulseaek * geunrosodeukgeumaek / yeonbong;
//		System.out.println(handoaek);
//		
//		//보험료 + 의료비 + 교육비 
//		int sumHando = insuranceSeaekgongje + medicalSeaekgongje + educationSeaekgongje;
//		System.out.println(sumHando);
		
		//특별세엑공제
		int teukbyeolseaekgongje = insuranceSeaekgongje + medicalSeaekgongje + educationSeaekgongje + donationSeaekgongje;
		System.out.println("특별세엑 공제 : " + teukbyeolseaekgongje);
		
		//자녀세액공제
		
		//기본공제대상 자녀(손자·손녀 제외)로서 7세 이상의 자녀 수에 따라 세액공제
		int gibongongjedaesangjanyeo = 0;
		if(children == 1) {
			gibongongjedaesangjanyeo = 150000;
		} else if (children == 2) {
			gibongongjedaesangjanyeo = 300000;
		} else if (children >= 3) {
			gibongongjedaesangjanyeo = 300000 + (children -2) * 300000;
		}
		System.out.println("기본공제대상 자녀 : " + gibongongjedaesangjanyeo);
		
		//출산, 입양 공제 (이번 년도에 출산하거나 입양한 자녀)
		int chulsanibyanggongje = 0;
		if (chulsanibyangjanyeo == 1) {
			chulsanibyanggongje = 300000;
		} else if (chulsanibyangjanyeo == 2) {
			chulsanibyanggongje = 500000;
		} else if (chulsanibyangjanyeo >= 3) {
			chulsanibyanggongje = 700000;
		}
		System.out.println("출산/입양 공제 : " + chulsanibyanggongje);
		
		//자녀세액공제 = 기본공제대상 자녀 + 출산/입양공제
		int janyeoseaekgongje = gibongongjedaesangjanyeo + chulsanibyanggongje;
		System.out.println("자녀세액공제액 : " + janyeoseaekgongje);
	
		//월세세액공제
		int houseWithdraw = accRepo.sumCategoryWithdraw("jin", "주거", 2023); //23년 주거비 일단 월세로 퉁침
		int wolseseaekgongje = (int) (Math.min(houseWithdraw, 7500000) * 0.10);
		
		System.out.println("월세세액 공제 : " + wolseseaekgongje);
		
		//결정세액 = 산출세액 - 세금감면 - 세액공제
		int gyeoljeongseaek = sanchulseaek - (geunrosodeukseaekgongje + teukbyeolseaekgongje + wolseseaekgongje + janyeoseaekgongje);
		System.out.println("결정세액 : " + gyeoljeongseaek);
		
		///////////////////////////////4단계 끝/////////////////////////////////////
	
		// 납부/환급세액 = 결정세액 - 기납부세액
		//기납부세액의 계산 방법이 너무 복잡하고 이해가 안가서 입력받아 사용하기로 결정(회사에 말하면 알 수 있다고 함)
		int ginapbuseaek  =  informationForTaxList.get(0).getGinapbuseaek(); //입력받은 기납부세액
		
		System.out.println("기납부세액 : " + ginapbuseaek);
		
		//이 값이 - 이면 그 금액만큼 환급받는 것이고, + 인 경우 그 금액만큼 세금을 더 납부해야 한다. 
		int napbuhwangeupseaek = gyeoljeongseaek - ginapbuseaek;
		System.out.println("납부/환급 세액 : " + napbuhwangeupseaek);
		
		return napbuhwangeupseaek;
	}
	
	
}
