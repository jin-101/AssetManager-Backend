package com.shinhan.assetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shinhan.assetManager.dto.FinancialCompanyDTO;
import com.shinhan.assetManager.dto.SavingsDTO;
import com.shinhan.assetManager.dto.SavingsOptionDTO;
import com.shinhan.assetManager.repository.FinancialCompanyRepository;
import com.shinhan.assetManager.repository.SavingsOptionRepository;
import com.shinhan.assetManager.repository.SavingsRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootTest
public class SavingsTest {
	
	@Autowired
	FinancialCompanyRepository finCoRepo;
	@Autowired
	SavingsRepository savingsRepo;
	@Autowired
	SavingsOptionRepository savingsOptionRepo;
	
	@Test
	void insertSavingsOption() throws IOException {
		JSONArray optionList = makeJsonObject("optionList");
		int list_cnt = optionList.length();
		for(int i=0; i<list_cnt; i++) { 
			JSONObject jsonObj = optionList.getJSONObject(i);
			SavingsDTO savings = savingsRepo.findByFinPrdtCd(jsonObj.get("fin_prdt_cd").toString());
			if(savings != null) {
				SavingsOptionDTO savingsOption = SavingsOptionDTO.builder()
						.savings(savings)
						.intrRateType(jsonObj.get("intr_rate_type").toString())
						.intrRateTypeNm(jsonObj.get("intr_rate_type_nm").toString())
						.rsrvType(jsonObj.get("rsrv_type").toString())
						.rsrvTypeNm(jsonObj.get("rsrv_type_nm").toString())
						.saveTrm(Integer.parseInt(jsonObj.get("save_trm").toString()))
						.intrRate(jsonObj.get("intr_rate").toString().equals("null")? null : Double.parseDouble(jsonObj.get("intr_rate").toString()))
						.intrRate2(jsonObj.get("intr_rate2").toString().equals("null")? null : Double.parseDouble(jsonObj.get("intr_rate2").toString()))
						.build();
				savingsOptionRepo.save(savingsOption);
			}
		}
	}
	
	//@Test
	void insertSavings() throws IOException {
		JSONArray productList = makeJsonObject("baseList");
		int list_cnt = productList.length();
		for(int i=0; i<list_cnt; i++) { 
			JSONObject jsonObj = productList.getJSONObject(i);
			FinancialCompanyDTO finCo;
			finCo = finCoRepo.findById((String) jsonObj.get("fin_co_no")).orElse(null);
			if(finCo == null) {
				finCo = FinancialCompanyDTO.builder()
						.finCoNo((String) jsonObj.get("fin_co_no"))
						.korCoNm((String) jsonObj.get("kor_co_nm"))
						.build();
				finCoRepo.save(finCo);
			}
			
			SavingsDTO save = SavingsDTO.builder()
					.savingsId((i+1)+"")
					.finCo(finCo)
					.finPrdtNm(jsonObj.get("fin_prdt_nm").toString())
					.finPrdtCd(jsonObj.get("fin_prdt_cd").toString())
					.joinWay(jsonObj.get("join_way").toString())
					.mtrtInt(jsonObj.get("mtrt_int").toString())
					.spclCnd(jsonObj.get("spcl_cnd").toString())
					.joinDeny(jsonObj.get("join_deny").toString())
					.joinMember(jsonObj.get("join_member").toString())
					.etcNote(jsonObj.get("etc_note").toString())
					.maxLimit(jsonObj.get("max_limit").toString().equals("null") ? null : Integer.parseInt(jsonObj.get("max_limit").toString()))
					.dclsStrtDay(convertToDate(dateFormater((String)jsonObj.get("dcls_strt_day"))))
					.dclsEndDay(jsonObj.get("dcls_end_day").toString().equals("null") ? null : convertToDate(dateFormater((String)jsonObj.get("dcls_end_day"))))
					.build();
			savingsRepo.save(save);
		}
	}
	
	private JSONArray makeJsonObject(String key) throws IOException{
		String link = "https://finlife.fss.or.kr/finlifeapi/savingProductsSearch.json?auth=4073bfe158c38a0390adb0c571677ee4&topFinGrpNo=020000&pageNo=1";
		StringBuilder urlBuilder = new StringBuilder(link); /*URL*/
		   URL url = new URL(urlBuilder.toString());
		   HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		   conn.setRequestMethod("GET");
		   conn.setRequestProperty("Content-type", "application/json");
		   BufferedReader br;
		   if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			   br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		   } else {
			   br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		   }
		   
		   StringBuffer sb = new StringBuffer();
		   String line;
		   while ((line = br.readLine()) != null) {
		       sb.append(line);
		   }
		   String str = sb.toString();
		   JSONObject jsonObj = new JSONObject(str);
		   JSONObject jsonObj2 = (JSONObject) jsonObj.get("result");
		   
		   JSONArray arr = (JSONArray) jsonObj2.get(key);
		   
		   br.close();
		   conn.disconnect();
		   
		   return arr;
	}
	
	private static Date convertToDate(String sdate) {
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date sqlDate = null;
		try {
			java.util.Date d = sdf.parse(sdate); //util의 date만들기
			sqlDate = new Date(d.getTime()); //sql의 date만들기
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sqlDate;
	}
	
	private static String dateFormater(String sDate) {
		StringBuffer buf = new StringBuffer(sDate);
		buf.insert(6, "-");
		buf.insert(4, "-");
		String str = buf.toString();
		return str;
	}
}
