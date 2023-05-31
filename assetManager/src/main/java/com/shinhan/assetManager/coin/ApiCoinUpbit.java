package com.shinhan.assetManager.coin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

// 업비트 (JSON) - 총 소요시간 : 15초
// (1) https://api.upbit.com/v1/market/all
// (2) https://api.upbit.com/v1/ticker?markets=KRW-BTC
@Component // ★ 이 ApiCoinUpbit라는 bean을 인식 및 사용할 수 있게끔(ex. @Autowired) 컴포넌트로 선언해줘야 함!
public class ApiCoinUpbit {

	// 테스트용 main
	public static void main(String[] args) throws IOException {
		ApiCoinUpbit apiCoin = new ApiCoinUpbit();
		List<String> marketList = apiCoin.getMarketList();
		System.out.println("업비트 코인 개수: "+marketList.size());
		List<CoinUpbitDTO> coinList = apiCoin.getCoinPrice(marketList);
		
		// 테스트용 출력
		// ★★★★★ 출력이 안 되는게 아니고 ㅋㅋ;; 저 forEach를 다 돌고 출력되는 거라서 기다려야됐음
		coinList.forEach(coin->{
			System.out.println(coin);
		});
	}

	// (2) 각 코인의 시세(JSON)를 => 바로 DTO로 매핑 ??????????
	public List<CoinUpbitDTO> getCoinPrice(List<String> marketList) throws IOException {
		
		List<CoinUpbitDTO> coinList = new ArrayList<>();
		
		marketList.forEach(market->{
			StringBuilder urlBuilder = new StringBuilder("https://api.upbit.com/v1/ticker");
			
	        try {
	        	Thread.sleep(100); // loop 돌릴 때 term을 주기 위해 
				urlBuilder.append("?" + URLEncoder.encode("markets","UTF-8") + "=" + market);
				URL url = new URL(urlBuilder.toString());
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        conn.setRequestProperty("Content-type", "application/json");
		        //System.out.println("Response code: " + conn.getResponseCode());
		        BufferedReader rd;
		        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
		            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		        } else {
		            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		        }
		        StringBuilder sb = new StringBuilder();
		        String line;
		        while ((line = rd.readLine()) != null) {
		            sb.append(line);
		        }
		        rd.close();
		        conn.disconnect();
		        String jsonStr = sb.toString();
		        
		        // JSON to DTO(class) => ★(정정)일단은 잭슨 lib 써서 매핑을 굳이 할 필요가 없을 듯 
		        // 이유 1. 아직은 필요한 데이터 종류가 2개밖에 없기도 하고 
		        // 이유 2. 이렇게 하면 JSON 데이터에 있는 key의 이름을 써야 하는데.. 업비트,빗썸,바이낸스 다 달라서 헷갈리더라고
		        
		        
		        // (정정2) 
		        // **이 부분 모듈화 할까?
		        JSONArray jsonArr = new JSONArray(jsonStr);
		        JSONObject jsonObj = jsonArr.getJSONObject(0);
		        String symbol = (String) jsonObj.get("market");
		        String coinName = symbol.substring(4, symbol.length()) + "_upbit"; // _upbit : @Id를 위해 추가
		        
		        
		        ObjectMapper mapper = new ObjectMapper();
		        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		        CoinUpbitDTO mappedDTO = mapper.readValue(jsonObj.toString(), CoinUpbitDTO.class); // (A) 여기서 바꿔준게 return을 List가 아니라 class 하나만 오게끔 arg를 배열=>객체로 바꿨음
//		        Double prev_closing_price = (Double) jsonObj.get("prev_closing_price"); // ★ 예외발생(String=>Double로 변경): java.lang.ClassCastException: class java.math.BigDecimal cannot be cast to class java.lang.String
		        mappedDTO.setCoinName(coinName); // 
		        coinList.add(mappedDTO); // (B)그리고 그 각 DTO를 전역변수인 coinList에 담아줘야 return coinList를 할 수 있더라고  
		        
		        System.out.println("업비트 진행중. 반복이 끝나면 출력됨 ㄱㄷ");
		        
			} catch (UnsupportedEncodingException e) {
				System.out.println("UnsupportedEncodingException 발생");
				e.printStackTrace();
			} catch (MalformedURLException e) {
				System.out.println("MalformedURLException 발생");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IOException 발생");
				e.printStackTrace();
			} catch (InterruptedException e) { // Thread.sleep() 때문에 추가했음
				System.out.println("InterruptedException 발생");
				e.printStackTrace();
			} 
		});
		
		return coinList; // (C)리턴 성공.. ㅅㅅ
	}

	// (1) 먼저 marketList 가져오고
	public List<String> getMarketList() throws IOException {
		URL url = new URL("https://api.upbit.com/v1/market/all"); /* URL */
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		//System.out.println("Response code: " + conn.getResponseCode());
		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();
		String str = sb.toString();

		JSONArray jsonArr = new JSONArray(str);
		List<String> marketList = new ArrayList<>();
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			String market = (String) jsonObj.get("market");

			// ★여기서! 앞이 KRW로 시작하는 놈들만 넣는 조건 필요함.
			// BTC-OOO, ETC-OOO 이런 건 필요없음 (우린 원화 대비 가격만을 조회할 것이기에)
			if (market.contains("KRW")) {
				marketList.add(market);
			}
		}

		return marketList;
	}

}
