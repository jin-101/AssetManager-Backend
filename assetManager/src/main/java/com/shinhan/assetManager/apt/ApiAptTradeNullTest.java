package com.shinhan.assetManager.apt;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// 국토교통부_아파트매매 실거래 상세 자료 (XML)
// 참조 : https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=nonamed0000&logNo=220988048654 (★ XML 파싱법)
// 참조 : https://blog.naver.com/hisukdory/50085040663
@Component
public class ApiAptTradeNullTest {
	
	StringBuilder sb = null; 

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException,
			TransformerFactoryConfigurationError, TransformerException, InterruptedException {
		ApiAptTradeNullTest apiAptTrade = new ApiAptTradeNullTest();
		List<AptDTO> aptList = 
				apiAptTrade.getAptList();
	}
	
	// 1. 메인 메소드 
	// 아파트 리스트 얻기 
	public List<AptDTO> getAptList() throws InterruptedException {
		String[] areaCodeList = AptParamList.getAreaCodeList();
		List<String> dateList = AptParamList.getDateList();
		List<AptDTO> aptList = new ArrayList<>();
		
		for(int i=0; i<1; i++) { 
			String date = dateList.get(i);
			System.out.println("계약연월 : "+date);
			
			for(int j=0; j<1; j++) { 
				Thread.sleep(3000); 
				String areaCode = areaCodeList[j]; 
				String url = urlBuild(areaCode, date);
				String totalCount = getTotalCount(url); 
				
				url += "&numOfRows="+totalCount; 
				System.out.println(url);
				
				aptList = parsingXML(url, totalCount); 
				aptList.stream().forEach(apt->{
					System.out.println(apt.toString());
				});
				System.out.println("-------------------------------------------------------------"); 
			}
		}
		return aptList;
	} 
	
	// 5. 서브 메소드
	// 조건에 따라 URL 생성
	public String urlBuild(String areaCode, String date) {
		List<String> serviceKeyList = AptParamList.getServiceKeyList();
		String url = null;
		
		StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev"); /*URL*/
		urlBuilder.append("?serviceKey=FW0FOOhX%2BZP3g%2FeWPgVS8l9opit3y%2Ba1AGvfL2inuxOkfz3jI1OLghrdJYfknlXR3ZurO3Q%2FEviXMszMrIwgtg%3D%3D"); /*Service Key*/
		urlBuilder.append("&LAWD_CD=" + "41570"); // ★ 건축년도 null인 놈이 있는 url 			
		urlBuilder.append("&DEAL_YMD=" + "202301"); // ★ 건축년도 null인 놈이 있는 url 		
		url = urlBuilder.toString();
		
		return url;
	}
	
	// 2. 서브 메소드
	// ★ API 요청할 url에 totalCount가 필요해서 이를 얻는 함수를 만들었음.
	public String getTotalCount(String url) {
		DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		String totalCount = "10"; 
		
		try {
			dBuilder = dbFactoty.newDocumentBuilder();
			doc = dBuilder.parse(url);
			doc.getDocumentElement().normalize(); 
			
			totalCount = doc.getElementsByTagName("totalCount").item(0).getTextContent();
			System.out.println("매매된 아파트의 수는 = " + totalCount);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return totalCount;
	}

	// 3. 서브 메소드
	// XML 파싱 및 아파트DTO 만들기
	public List<AptDTO> parsingXML(String url, String totalCount) {

		System.out.println("parsingXML에서의 totalCount : " + totalCount);	
		List<AptDTO> aptList = new ArrayList<>();

		DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		try {
			dBuilder = dbFactoty.newDocumentBuilder();
			doc = dBuilder.parse(url);
			doc.getDocumentElement().normalize(); 

			NodeList nList = doc.getElementsByTagName("item"); 
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				AptDTO apt = new AptDTO();
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					String price = getTagValue("거래금액", eElement);
					String constructionYear = getTagValue("건축년도", eElement); // ★ null 체크 거쳤음 : 건축년도 태그가 아예 없는 놈이 있어서 (url : LAWD_CD=41570&DEAL_YMD=202301)
					String year = getTagValue("년", eElement);
					String month = getTagValue("월", eElement);
					String dong = getTagValue("법정동", eElement);
					String aptName = getTagValue("아파트", eElement);
					String netLeasableArea = getTagValue("전용면적", eElement);
					String floors = getTagValue("층", eElement);
					String areaCode = getTagValue("지역코드", eElement);
					String tradeDate = null;
					StringBuilder sb = new StringBuilder();
					if(Integer.parseInt(month) < 10) { // ★ DB에서 다루기 쉽게끔 연+월
						tradeDate = sb.append(year).append("0").append(month).toString();
					}else {
						tradeDate = sb.append(year).append(month).toString();
					}
					apt.setAptName(aptName);
					apt.setAreaCode(areaCode);
					apt.setConstructionYear(constructionYear);
					apt.setDong(dong);
					apt.setFloors(floors);
					apt.setNetLeasableArea(netLeasableArea);
					apt.setPrice(price.replace(",", "")); // , 지워주기 위해 (**지우고 넣을지, 그냥 넣을지 상황봐서 선택)
					apt.setTradeDate(tradeDate);
					aptList.add(apt);
				} // for end
			} // if end
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return aptList;
	}

	// 4. 서브 메소드
	// ★ tag값의 정보를 가져오는 메소드 생성하였음.
	private static String getTagValue(String tag, Element eElement) {
		String tagValue = null;
		NodeList nodeList = eElement.getElementsByTagName(tag); // tag == 건축년도
		NodeList childNodeList = null;
		Node nValue = null;
		
		// ★★★★★ 해당 tag가 없을 때 값이 어떻게 들어오는지 봤더니
		if(tag.equals("건축년도")) {
			System.out.println(nodeList); // (1) 그냥 nList는 객체이고 (com.sun.org.apache.xerces.internal.dom.DeepNodeListImpl@2e222612)
			System.out.println(nodeList.item(0)); // (2) nList.item(0)이 null값으로 찍히며 ([건축년도: null] => null (우측의 값이 건축년도 태그 없는 경우))
			System.out.println(nodeList.getLength()); // (3) nList.getLength()의 경우엔 0이 찍힘 (1 => 0)
		}
		
		// ★★★★★ 따라서 null 체크를 하는 방법으로는 item(0) 또는 getLength()를 쓰면 되겠다!
		if(nodeList.getLength() == 0) { // <=> nList.item(0) == null 로도 치환가능
			tagValue = "";
		}else {
			childNodeList = nodeList.item(0).getChildNodes();
			nValue = (Node) childNodeList.item(0);
			
			if (nValue == null) {
				tagValue = ""; // 원래 null 리턴하도록 돼있었는데 ""로 바꿔줌 (나중에 JPA가 null 인식(그건 스프링프레임워크인가..?))
			}
			
			tagValue = nValue.getNodeValue().trim();
		}
		
		return tagValue; 
	}
	
}

