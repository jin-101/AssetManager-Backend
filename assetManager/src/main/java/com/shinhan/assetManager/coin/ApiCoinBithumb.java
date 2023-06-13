package com.shinhan.assetManager.coin;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// 빗썸 (JSON) - 총 소요시간 : 20초
// 전체조회 : "https://api.bithumb.com/public/ticker/ALL_KRW"
@Component
public class ApiCoinBithumb {

	public static void main(String[] args) throws IOException, InterruptedException {
		ApiCoinBithumb apiCoin = new ApiCoinBithumb();
		List<CoinBithumbDTO> coinList = apiCoin.getCoinPrice();
		
		// 테스트용 출력
		coinList.forEach(coin->{
			System.out.println(coin);
		});
	}

	// 
	public List<CoinBithumbDTO> getCoinPrice() throws IOException, InterruptedException {
		
		List<CoinBithumbDTO> coinList = new ArrayList<>();
		
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.bithumb.com/public/ticker/ALL_KRW"))
				.header("accept", "application/json").method("GET", HttpRequest.BodyPublishers.noBody()).build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		String responseBody = response.body(); // 아 이게 데이터가 너무 많아서 콘솔 창에 안 뜨나 본데..? (1개 시세는 뜨는데, 전체 시세 요청하니까 안 뜨네)

		// 빗썸 코인 리스트 (Set) 
		JSONObject jsonObj = new JSONObject(responseBody);
		JSONObject jsonObj2 = (JSONObject) jsonObj.get("data");
		Set<String> coinSet = jsonObj2.keySet();
		coinSet.remove("date"); // JSON 데이터 맨 아래에 "date"도 있어서 얘가 딸려오더라고. 그래서 지워줬음. 
		System.out.println(coinSet);
		
		// 
		coinSet.forEach(coinName -> {
			JSONObject jsonObj3 = new JSONObject();
			jsonObj3 = (JSONObject) jsonObj2.get(coinName);
			String jsonStr = jsonObj3.toString();
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			try {
				Thread.sleep(100); // loop 돌릴 때 term을 주기 위해
				CoinBithumbDTO mappedDTO;
				mappedDTO = mapper.readValue(jsonStr, CoinBithumbDTO.class); // ★ jsonStr : 이 부분.. String으로 바꿔준 걸 arg로 줘야 하더라고 (그냥 JSONObject, JSONArray 형태로는 못 받더라)
				String coinNameForId = coinName+"_bithumb";
				mappedDTO.setCoinName(coinNameForId); // (1) 각 코인의 이름 (keySet 中)
				mappedDTO.setPrev_closing_price(mappedDTO.getPrev_closing_price()); // (2) 전일종가
				coinList.add(mappedDTO);
				
				System.out.println("빗썸 진행중. 반복이 끝나면 출력됨 ㄱㄷ");
				
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) { 
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		});
		
		return coinList;
	}
}
//		StringBuilder urlBuilder = new StringBuilder("https://api.bithumb.com/public/ticker/ALL_KRW");
//		try {
//			Thread.sleep(500); // loop 돌릴 때 term을 주기 위해
//			URL url = new URL(urlBuilder.toString());
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("GET");
//			conn.setRequestProperty("Content-type", "application/json");
//			System.out.println("Response code: " + conn.getResponseCode());
//			BufferedReader rd;
//			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			} else {
//				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//			}
//			StringBuilder sb = new StringBuilder();
//			String line;
//			while ((line = rd.readLine()) != null) {
//				sb.append(line);
//			}
//			rd.close();
//			conn.disconnect();
//			System.out.println(sb.toString());

		// JSON 껍질 벗기기
//			JSONObject jsonObj = new JSONObject(sb.toString());
//			System.out.println(jsonObj);
//			JSONObject jsonObj2 = (JSONObject) jsonObj.get("data");
//			System.out.println(jsonObj2);
//			JSONObject jsonObj3 = (JSONObject) jsonObj2.get("BTC");
//			System.out.println(jsonObj3);
//			String jsonStr = jsonObj3.toString();
//			System.out.println(jsonStr);

		// JSON to DTO(class)
//			ObjectMapper mapper = new ObjectMapper();
//			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//			List<CoinDTO> coinList = Arrays.asList(mapper.readValue(jsonStr, CoinDTO[].class));
//			coinList.forEach(coin -> {
//				System.out.println(coin.toString()); // @ToString 먹네
//				System.out.println(coin.getPrev_closing_price()); // 전일종가
//			});
//		} catch (UnsupportedEncodingException e) {
//			System.out.println("UnsupportedEncodingException 발생");
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			System.out.println("MalformedURLException 발생");
//			e.printStackTrace();
//		} catch (IOException e) {
//			System.out.println("IOException 발생");
//			e.printStackTrace();
//		} catch (InterruptedException e) { // Thread.sleep() 때문에 추가했음
//			System.out.println("InterruptedException 발생");
//			e.printStackTrace();
//		}
//	}
