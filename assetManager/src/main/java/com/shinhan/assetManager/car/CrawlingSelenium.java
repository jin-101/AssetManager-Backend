//자동차 365검색 크롤링

package com.shinhan.assetManager.car;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CrawlingSelenium {
    private WebDriver driver;

    private static final String url = "https://www.car365.go.kr/web/contents/usedcar_carcompare.do";
    public Map<String,String> process(String carNo) {
        System.setProperty("webdriver.chrome.driver", "C:\\workspace-SpringBoot\\chromedriver_win32\\chromedriver.exe");
        //크롬 드라이버 셋팅 (드라이버 설치한 경로 입력)
        Map<String,String> obj = null;
        driver = new ChromeDriver();
        //브라우저 선택

        try {
        	 obj = getDataList(carNo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.close();	//탭 닫기
        driver.quit();	//브라우저 닫기
        return obj;
    }


    /**
     * data가져오기
     */
    private  Map<String,String> getDataList(String carNo) throws InterruptedException {
        Map<String,String> obj = new HashMap<>();

        driver.get(url);    //브라우저에서 url로 이동한다.
        Thread.sleep(1000); //브라우저 로딩될때까지 잠시 기다린다.
        WebElement tab2 = driver.findElement(By.id("searchStr"));
        tab2.sendKeys(carNo);  //372저1164 //229더5382
        
        WebElement tab1 = driver.findElement(By.xpath("//*[@id=\"wrap\"]/div/div[2]/div[2]/div[2]/a"));
        tab1.click();
        
   
        List<WebElement> elements = driver.findElements(By.cssSelector("#usedcarcompare_data td"));
        String[] keyList = {"className","year","price"};
        for(int i=0; i<elements.size(); i++) {
        	if(i%2==0 && i<6) {
        		obj.put(keyList[i/2], elements.get(i).getText());
        	}
        }
        return obj;
    }
}
