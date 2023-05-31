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

// 바이낸스 (JSON) - 총 소요시간 : 0초
// https://api.binance.com/api/v3/ticker/price
@Component
public class ApiCoinBinance {
	// ★ 바이낸스는 걍 빼버릴까?? => 일단 빼자 (코인명에 이상한 애들이 몇몇 있어서.. ex.UP, DOWN)
	// ★ KRW가 아니라 USDT로 계산하기에 이 부분을 고려해야..
	public static void main(String[] args) throws IOException {
		ApiCoinBinance apiCoin = new ApiCoinBinance();
		List<CoinBinanceDTO> coinList = apiCoin.getCoinPrice();
		
		// cf. 우리 환율 데이터 있으니까
		// (1) 환율(USD/KRW) 곱해준 가격을 보여줘도 되고
		// (2) 아니면 그냥 1달러에 대한 가격 보여줘도 되고 
		coinList.forEach(coin->{
			System.out.println(coin);
		});
	}
	
	// (1) 
	public List<CoinBinanceDTO> getCoinPrice() throws IOException {
        URL url = new URL("https://api.binance.com/api/v3/ticker/price"); /*URL*/
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
        String str = sb.toString();
        
        JSONArray jsonArr = new JSONArray(str);
        List<CoinBinanceDTO> coinList = new ArrayList<>();
        
        for(int i=0; i<jsonArr.length(); i++) {
        	JSONObject jsonObj = jsonArr.getJSONObject(i);
        	String symbol = (String) jsonObj.get("symbol");
        	int endIndex = symbol.length(); // substring 동작 방식이 구글링 결과가 아니라 그때 쌤이 알려주신 그게 맞는 거 같은데??
        	String usdt = symbol.substring(endIndex-4, endIndex);
        	
        	if(usdt.equals("USDT")) { // ★★ usdt == "USDT" (아휴 이 ㅄ아 String 비교하는데 ==을 쓰면 어뜩하냐;; .equals()를 써야지..)
        		String price = (String) jsonObj.get("price"); // ★ 위 조건을 해당하는 놈에 대해서만 '가격'을 얻어 (연산 속도도 생각)
        		CoinBinanceDTO coin = new CoinBinanceDTO();
        		String coinName = symbol.substring(0, endIndex-4) + "_binance"; // _binance : @Id 구분 위해 추가
        		coin.setCoinName(coinName);
        		coin.setPrice(price);
        		coinList.add(coin);
        		
        		System.out.println("바이낸스 진행중. 반복이 끝나면 출력됨 ㄱㄷ");
        	}
        }
		return coinList;
	}

}

//  //(2) 각 코인의 시세(JSON)를 => 바로 DTO로 매핑
//	public void getCoinPrice(List<String> symbolList) throws IOException {
//		symbolList.forEach(market->{
//			StringBuilder urlBuilder = new StringBuilder("https://api.binance.com/api/v3/ticker/price");
//	        try {
//	        	Thread.sleep(500); // loop 돌릴 때 term을 주기 위해 
//				urlBuilder.append("?" + URLEncoder.encode("markets","UTF-8") + "=" +market);
//				URL url = new URL(urlBuilder.toString());
//		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		        conn.setRequestMethod("GET");
//		        conn.setRequestProperty("Content-type", "application/json");
//		        System.out.println("Response code: " + conn.getResponseCode());
//		        BufferedReader rd;
//		        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//		            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//		        } else {
//		            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//		        }
//		        StringBuilder sb = new StringBuilder();
//		        String line;
//		        while ((line = rd.readLine()) != null) {
//		            sb.append(line);
//		        }
//		        rd.close();
//		        conn.disconnect();
//		        String jsonStr = sb.toString();
//		        System.out.println("syso: "+jsonStr);
//		        
//		        // JSON to DTO(class)
//		        ObjectMapper mapper = new ObjectMapper();
//		        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		        List<CoinDTO> coinList = Arrays.asList(mapper.readValue(jsonStr, CoinDTO[].class));
//		        coinList.forEach(coin->{
//		        	System.out.println(coin.toString()); // @ToString 먹네
//		        	System.out.println(coin.getPrev_closing_price()); // 전일종가
//		        });
//			} catch (UnsupportedEncodingException e) {
//				System.out.println("UnsupportedEncodingException 발생");
//				e.printStackTrace();
//			} catch (MalformedURLException e) {
//				System.out.println("MalformedURLException 발생");
//				e.printStackTrace();
//			} catch (IOException e) {
//				System.out.println("IOException 발생");
//				e.printStackTrace();
//			} catch (InterruptedException e) { // Thread.sleep() 때문에 추가했음
//				System.out.println("InterruptedException 발생");
//				e.printStackTrace();
//			} 
//		});
//	}

















