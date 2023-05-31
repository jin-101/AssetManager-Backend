package com.shinhan.assetManager;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shinhan.assetManager.dto.CarCompanyDTO;
import com.shinhan.assetManager.dto.CarInfomationDTO;
import com.shinhan.assetManager.dto.CarModelDTO;
import com.shinhan.assetManager.repository.CarCompanyRepository;
import com.shinhan.assetManager.repository.CarInfomationRepository;
import com.shinhan.assetManager.repository.CarModelPRepository;

//JUNIT으로 단위test하기
@SpringBootTest
class CarTest {
	private WebDriver driver;

    private static String CralwingURL = "https://www.kbchachacha.com/public/search/main.kbc#!?page=1&sort=-orderDate&makerCode=";
    private static String MAKERURL = "https://www.kbchachacha.com/public/search/carMaker.json?page=1&sort=-orderDate";
    private static String CLASSURL = "https://www.kbchachacha.com/public/search/carClass.json?page=1&sort=-orderDate&makerCode=";
    private static String NAMEURL = "https://www.kbchachacha.com/public/search/carName.json?page=1&sort=-orderDate&makerCode=";
    private static String MODELURL = "https://www.kbchachacha.com/public/search/carModel.json?page=1&sort=-orderDate&makerCode=";
	    
	
	@Autowired
	CarCompanyRepository carCompanyRepo;
	@Autowired
	CarModelPRepository carModelRepo;
	@Autowired
	CarInfomationRepository carInfoRepo;
	
	@Test
	public void process() throws IOException {
        System.setProperty("webdriver.chrome.driver", "C:\\workspace-SpringBoot\\chromedriver_win32\\chromedriver.exe");
        //크롬 드라이버 셋팅 (드라이버 설치한 경로 입력)
        driver = new ChromeDriver();
        //브라우저 선택
        try {
            getDataList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.close();	//탭 닫기
        driver.quit();	//브라우저 닫기
    }
	
	 private List<Object> makeJsonObject (String link, String[] keys) throws IOException {
	   StringBuilder urlBuilder = new StringBuilder(link); /*URL*/
		   URL url = new URL(urlBuilder.toString());
		   HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		   conn.setRequestMethod("GET");
		   conn.setRequestProperty("Content-type", "application/json");
		   //System.out.println("Response code: " + conn.getResponseCode());
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
		   List<Object> totalList = new ArrayList<Object>();
		   for(String key : keys) {
			   JSONArray arr = (JSONArray) jsonObj2.get(key);
			   totalList.addAll(arr.toList());
		   }
		   
		   br.close();
		   conn.disconnect();
		   
		   return totalList;
	 }
	 
	 private void getDataList() throws InterruptedException, IOException {
	    	
 		String[] makerKey= {"수입","국산"};
	 	    String[] classAndNameKey= {"code"};
	 	    String[] modelKey= {"codeGrade"};
	 	    
 		List<Object> MakerList = makeJsonObject(MAKERURL, makerKey);
 		
 		int tempTotalCount=0;
	 	int carCount = 1;    
 		for(Object makers : MakerList) {
	 	    	Map newMaker = (Map)makers;
	 	    	
	 	    	CarCompanyDTO company = CarCompanyDTO.builder()
	 					.companyName((String) newMaker.get("makerName"))
	 					.build();
	 	    	carCompanyRepo.save(company);
	 	    	
	 	    	List<Object> classList = makeJsonObject(CLASSURL + newMaker.get("makerCode"), classAndNameKey);
	 		
	 	    	for(Object classs : classList) {
	 	    		Map newClass = (Map)classs;
	 	    		List<Object> nameList = makeJsonObject(NAMEURL + newClass.get("makerCode") + "&classCode=" + newClass.get("classCode"), classAndNameKey);
	 	
	 	    		for(Object names : nameList) {
	 	    			Map newName = (Map)names;
	 	    			List<Object> modelList = makeJsonObject(MODELURL + newName.get("makerCode") + "&classCode=" + newName.get("classCode") + "&carCode="+ newName.get("carCode"), modelKey);
		 		
	 	    			List<String> duplCheck = new ArrayList<>();
	 	    			for(Object models: modelList) {
	 	    				Map newModel = (Map)models;
	 	    				
	 	    				CarCompanyDTO companyVO = carCompanyRepo.findByCompanyName((String)newModel.get("makerName"));
		 	    			if(companyVO!=null) {
		 	    				CarModelDTO model = CarModelDTO.builder()
		 	   	 					.className((String) newModel.get("className"))
		 	   	 					.carName((String) newModel.get("carName"))
		 	   	 					.modelName((String) newModel.get("modelName"))
		 	   	 					.gradeName((String) newModel.get("gradeName"))
		 	   	 					.carCompany(companyVO)
		 	   	 					.build();
		 	   	 	    		carModelRepo.save(model);
		 	    			}
	 	    				
				 	    	System.out.println(newModel.get("makerName") + " " + newModel.get("className") + " " + newModel.get("carName") + " "+ newModel.get("modelName")+ " "+ newModel.get("gradeName"));
				 	    	String modelCode = (String) newModel.get("modelCode");
				 	    	
				 	    	if(modelCode!="" && !duplCheck.contains(modelCode)) {
				 	    		duplCheck.add((String) newModel.get("modelCode"));
				 	    		driver.get(CralwingURL + newModel.get("makerCode") + "&classCode=" + newModel.get("classCode") + "&carCode="+ newModel.get("carCode")+"&modelCode="+modelCode);
				 	    		Thread.sleep(1500);
				 	    		List<WebElement> elements = driver.findElements(By.cssSelector(".generalRegist .area"));
		 	    				int tempCount = 0;
		 	    				for (WebElement element : elements) { //자동차 1대 전체 정보
						 	    	String yearText= element.findElement(By.cssSelector(".first")).getText();
						 	    	String yearT = yearText.substring(0,yearText.indexOf("년"));
		 	    					String price =  element.findElement(By.cssSelector(".pay")).getText();
		 	    				
		 	    					if(price.indexOf("리스")!=-1) continue;
		 	    					
		 	    					Map<String, String> oneCar = new HashMap<>(); ///맵 만들어서 오브젝트로 만들어보기
					 	    		//차 제조회사 데이터 넣기
						 	    	oneCar.put("company", (String) newModel.get("makerName"));
						 	    	//차 클래스 데이터 넣기
					 	    		oneCar.put("carClass", (String) newModel.get("className"));
					 	    		//차 타입 데이터 넣기
						 	    	oneCar.put("type", (String) newModel.get("useCodeName"));
					 	    		//차 이름 데이터 넣기
						 	    	oneCar.put("carName", (String) newModel.get("carName"));
					 	    		//차 모델 데이터 넣기
						 	    	oneCar.put("modelName", (String) newModel.get("modelName"));
						 	    	//차 등급 데이터 넣기
						 	    	oneCar.put("gradeName", (String) newModel.get("gradeName"));
						 	    	//차 연식 데이터 넣기
		 	    					oneCar.put("year", yearT);
		 	    					//차 가격 데이터 넣기
		 	    					oneCar.put("price",price.substring(0, price.indexOf("만원")).replace(",", "") + "0000");
		 	    					CarModelDTO modelVO = carModelRepo.findByClassNameAndCarNameAndModelName((String)newModel.get("className"),(String)newModel.get("carName"),(String)newModel.get("modelName"));
		 	    					if(modelVO!=null) {
		 	    						System.out.println(modelVO.toString() + "---------------------" + carCount);
				 	    				CarInfomationDTO info = CarInfomationDTO.builder()
				 	    					.infomationId(carCount+"")
				 	   	 					.carModel(modelVO)
				 	   	 					.type((String) newModel.get("useCodeName"))
				 	   	 					.year(yearT)
				 	   	 					.price(Integer.parseInt(price.substring(0, price.indexOf("만원")).replace(",", "") + "0000"))
				 	   	 					.build();
				 	    				carInfoRepo.save(info);
				 	    				carCount++;
				 	    			}
		 	    					
		 	    					
		 	    					tempCount ++;
		 	    					System.out.println(oneCar.toString() + "  "+ oneCar.size());
			 	    			}
			 	    			System.out.println(tempCount + "------------------------------------------------------page");
			 	    			tempTotalCount = tempTotalCount + tempCount;
				 	    	}
	 	    			}	
	 	    			  System.out.println(tempTotalCount);
	 	    		}
	 	    	}
	 	    }
	 }

}
