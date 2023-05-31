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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// 국토교통부_아파트매매 실거래 상세 자료 (XML)
// 참조 : https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=nonamed0000&logNo=220988048654 (★★★)
// 참조 : https://blog.naver.com/hisukdory/50085040663
public class ApiAptTradeBackup {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException,
			TransformerFactoryConfigurationError, TransformerException, InterruptedException {
		ApiAptTradeBackup apiAptTrade = new ApiAptTradeBackup();
		List<AptDTO> aptList = apiAptTrade.getAptList();
		
//		// 테스트용 출력
//		aptList.forEach(apt->{
//			System.out.println(apt);
//		});

		// bb();
	}
	
	// 1. 메인 (아파트 리스트 얻기) 
	public List<AptDTO> getAptList() throws InterruptedException {
		String[] areaList = AptParamList.getAreaCodeList();
		List<String> dateList = AptParamList.getDateList();
		List<AptDTO> aptList = new ArrayList<>();
		
		for(int i=0; i<dateList.size(); i++) {
			Thread.sleep(1000);
			String date = dateList.get(i);
			System.out.println("계약연월 : "+date);
			
			StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev"); /*URL*/
			urlBuilder.append("?serviceKey=FW0FOOhX%2BZP3g%2FeWPgVS8l9opit3y%2Ba1AGvfL2inuxOkfz3jI1OLghrdJYfknlXR3ZurO3Q%2FEviXMszMrIwgtg%3D%3D"); /*Service Key*/
			urlBuilder.append("&LAWD_CD="+"11110"); // 			
			urlBuilder.append("&DEAL_YMD="+date); 
			
			String totalCount = getTotalCount(urlBuilder.toString()); // (1) 먼저 각 XML데이터의 totalCount를 알아내고
			urlBuilder.append("&numOfRows="+totalCount); // (2) URL에 이어 붙이고
			aptList = parsingXML(urlBuilder.toString(), totalCount); // (3) totalCount를 이용해서, 1페이지에 모든 거래내역이 들어올 수 있게 처리!! 
			aptList.stream().forEach(apt->{
				System.out.println(apt);
			});
		}
		
		return aptList;
	}
	
	// 2. ★ API 요청할 url에 totalCount가 필요해서 이를 얻는 함수를 만들었음.
	public String getTotalCount(String url) {
		DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		String totalCount = "10"; // 일단 10으로 고정
		
		try {
			dBuilder = dbFactoty.newDocumentBuilder();
			doc = dBuilder.parse(url);
			doc.getDocumentElement().normalize(); // nomalize() : DOM Tree가 XML문서의 구조대로 완성됨
			
			// ★ totalCount 얻기
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

	// 3. XML 파싱 및 아파트DTO 만들기
	public List<AptDTO> parsingXML(String url, String totalCount) {

		System.out.println("parsingXML에서의 totalCount : " + totalCount);	
		List<AptDTO> aptList = new ArrayList<>();
		// # 1. 파싱할 URL 준비

		// # 2. 페이지에 접근해줄 Document객체 생성
		DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		try {
			dBuilder = dbFactoty.newDocumentBuilder();
			doc = dBuilder.parse(url);
			doc.getDocumentElement().normalize(); // nomalize() : DOM Tree가 XML문서의 구조대로 완성됨

			// # 3. 파싱할 정보가 있는 tag에 접근
			NodeList nList = doc.getElementsByTagName("item"); // System.out.println("파싱할 리스트 수 : "+ nList.getLength());
			
			// # 4. list에 담긴 데이터 출력하기
			// - 위에 담긴 list를 반복문을 이용하여 출력한다.
			// - getTextContent() : 전체 정보
			// - getTagValue("tag", element) : 입력한 tag 정보(따로 메소드를 정의해줘야 한다. 맨 아래 전체소스코드에서 확인)
			for (int temp = 0; temp < nList.getLength(); temp++) {
				AptDTO apt = new AptDTO();
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					String price = getTagValue("거래금액", eElement);
					String constructionYear = getTagValue("건축년도", eElement);
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

	// 4. ★ tag값의 정보를 가져오는 메소드 생성하였음.
	private static String getTagValue(String tag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		if (nValue == null)
			return null;
		return nValue.getNodeValue().trim(); // trim() : **공백이 있는 text가 몇개 있어서 추가
	}

	
	// http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTrade?serviceKey=FW0FOOhX%2BZP3g%2FeWPgVS8l9opit3y%2Ba1AGvfL2inuxOkfz3jI1OLghrdJYfknlXR3ZurO3Q%2FEviXMszMrIwgtg%3D%3D&LAWD_CD=11110&DEAL_YMD=202304
	// # 진이형 인증키 : MQhlCw%2BaTNO6ltJfUJ5%2FTQ6azf3epMVtgix5SiWLTUIXKspVH0fN5amdLsV78m8Qvq0Bg18OU2CgIJktlLEYJQ%3D%3D

	
	
	
	
	
	
	// XML 파싱 및 태그의 값 가져오기
	public static void test() throws ParserConfigurationException, SAXException, IOException {
		// # 1. 파싱할 URL 준비
		String url = "http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTrade?serviceKey=FW0FOOhX%2BZP3g%2FeWPgVS8l9opit3y%2Ba1AGvfL2inuxOkfz3jI1OLghrdJYfknlXR3ZurO3Q%2FEviXMszMrIwgtg%3D%3D&LAWD_CD=11110&DEAL_YMD=202304";
		// XML 파싱
		// # 2. 페이지에 접근해줄 Document객체 생성
		DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
		Document doc = dBuilder.parse(url);
		// root tag
		doc.getDocumentElement().normalize(); // nomalize() : DOM Tree가 XML문서의 구조대로 완성됨
		// # 3. 파싱할 정보가 있는 tag에 접근
		NodeList nList = doc.getElementsByTagName("item"); // System.out.println("파싱할 리스트 수 : "+ nList.getLength());
		// # 4. list에 담긴 데이터 출력하기
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				System.out.println("######################");
				System.out.println("거래금액:" + getTagValue("거래금액", eElement));
				System.out.println("건축년도:" + getTagValue("건축년도", eElement));
				System.out.println("년:" + getTagValue("년", eElement));
				System.out.println("월:" + getTagValue("월", eElement));
				System.out.println("법정동:" + getTagValue("법정동", eElement));
				System.out.println("아파트:" + getTagValue("아파트", eElement));
				System.out.println("전용면적:" + getTagValue("전용면적", eElement));
				System.out.println("층:" + getTagValue("층", eElement));
				System.out.println("지역코드:" + getTagValue("지역코드", eElement));
			} // for end
		} // if end
	}
}

