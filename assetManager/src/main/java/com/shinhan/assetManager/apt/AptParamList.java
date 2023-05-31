package com.shinhan.assetManager.apt;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AptParamList {

	public static void main(String[] args) {
		List<String> dateList = AptParamList.getDateList();
		System.out.println(dateList);
		
		List<String> serviceKeyList = AptParamList.getServiceKeyList();
		serviceKeyList.forEach(key->{
			System.out.println(key);
		});

		// AptParamList.getCodeList();
	}
	
	public static List<String> getServiceKeyList() {
		List<String> serviceKeyList = new ArrayList<>();
		String key1 = "FW0FOOhX%2BZP3g%2FeWPgVS8l9opit3y%2Ba1AGvfL2inuxOkfz3jI1OLghrdJYfknlXR3ZurO3Q%2FEviXMszMrIwgtg%3D%3D"; // 나
		String key2 = "MQhlCw%2BaTNO6ltJfUJ5%2FTQ6azf3epMVtgix5SiWLTUIXKspVH0fN5amdLsV78m8Qvq0Bg18OU2CgIJktlLEYJQ%3D%3D"; // 진이형
		String key3 = "jOy%2FZT6R9K8jn0lECKfKjyXMZPOvP5dEOnXC3cnl6QRHO1BuOLAfvmrVvYOZ64mkiN6555Hq4dp1wBZt5Rd9xw%3D%3D"; // 찬혁
		String key4 = "J0GCS8cX3tTcYuPQWYV2QyYJt0Ac0I6hW%2FPT0RyciqBjLbhRUkWGdE9bKCChfBt5%2Bk9QO5xJXS17kcbg8YHAPg%3D%3D"; // 동열
		serviceKeyList.add(key1);
		serviceKeyList.add(key2);
		serviceKeyList.add(key3);
		serviceKeyList.add(key4);

		return serviceKeyList;
	}

	// 참조 : https://hantip.net/308 (엑셀 팁: 셀에 입력된 내용을 큰 따옴표로 묶는 방법)
	// 법정동코드: 250개
	public static String[] getAreaCodeList() {
		String[] codeArr = new String[250];
		codeArr = new String[] { "11680", "11740", "11305", "11500", "11620", "11215", "11530", "11545", "11350",
				"11320", "11230", "11590", "11440", "11410", "11650", "11200", "11290", "11710", "11470", "11560",
				"11170", "11380", "11110", "11140", "11260", "26440", "26410", "26710", "26290", "26170", "26260",
				"26230", "26320", "26530", "26380", "26140", "26500", "26470", "26200", "26110", "26350", "27200",
				"27290", "27710", "27140", "27230", "27170", "27260", "27110", "28710", "28245", "28200", "28140",
				"28177", "28237", "28260", "28185", "28720", "28110", "29200", "29155", "29110", "29170", "29140",
				"30230", "30110", "30170", "30200", "30140", "31140", "31170", "31200", "31710", "31110", "36110",
				"41820", "41281", "41285", "41287", "41290", "41210", "41610", "41310", "41410", "41570", "41360",
				"41250", "41190", "41135", "41131", "41133", "41113", "41117", "41111", "41115", "41390", "41273",
				"41271", "41550", "41173", "41171", "41630", "41830", "41670", "41800", "41370", "41463", "41465",
				"41461", "41430", "41150", "41500", "41480", "41220", "41650", "41450", "41590", "42150", "42820",
				"42170", "42230", "42210", "42800", "42830", "42750", "42130", "42810", "42770", "42780", "42110",
				"42190", "42760", "42720", "42790", "42730", "43760", "43800", "43720", "43740", "43730", "43770",
				"43150", "43745", "43750", "43111", "43112", "43114", "43113", "43130", "44250", "44150", "44710",
				"44230", "44270", "44180", "44760", "44210", "44770", "44200", "44810", "44131", "44133", "44790",
				"44825", "44800", "45790", "45130", "45210", "45190", "45730", "45800", "45770", "45710", "45140",
				"45750", "45740", "45113", "45111", "45180", "45720", "46810", "46770", "46720", "46230", "46730",
				"46170", "46710", "46110", "46840", "46780", "46150", "46910", "46130", "46870", "46830", "46890",
				"46880", "46800", "46900", "46860", "46820", "46790", "47290", "47130", "47830", "47190", "47720",
				"47150", "47280", "47920", "47250", "47840", "47170", "47770", "47760", "47210", "47230", "47900",
				"47940", "47930", "47730", "47820", "47750", "47850", "47111", "47113", "48310", "48880", "48820",
				"48250", "48840", "48270", "48240", "48860", "48330", "48720", "48170", "48740", "48125", "48127",
				"48123", "48121", "48129", "48220", "48850", "48730", "48870", "48890", "50130", "50110" };

		return codeArr;
	}

	// 참조 : https://itellyhood.tistory.com/74
	// [자바]Calendar클래스/ Time 패키지(달력,시간) _ 자바 쉽게 정리/특정날짜,현재시간 출력/add,set,get
	public static List<String> getDateList() {
		List<String> dateList = new ArrayList<String>();

		// Calendar 객체 이용
		Calendar cal = Calendar.getInstance();
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String today = sdf.format(now);

		for (int i = 0; i < 12; i++) {
			// year는 for문 하나 더 써서 여러개 만들어주면 될 듯
			int year = cal.get(Calendar.YEAR); // ★★★★★ 실제로 데이터 넣을 땐 1년 단위로 insert할 것!!

			int month = cal.get(Calendar.JANUARY) + i; // (정정)MONTH 안쓰고 JANUARY 쓰니까 (+ 1 안해도 되더라)

			StringBuilder date = null;
			StringBuilder sb = new StringBuilder();
			if (month < 10) {
				date = sb.append(year).append("0").append(month);
			} else if (month >= 10) {
				date = sb.append(year).append(month);
			}

			// 만약 현재날짜(202305)보다 큰 날짜는 못 넣게끔
			Integer todayNum = Integer.parseInt(today);
			Integer dateNum = Integer.parseInt(date.toString());
			if (dateNum < todayNum) { // 일단 <=가 아니라 <만 해서 23년5월치는 빼보자
				dateList.add(date.toString());
			}
		}
		return dateList;
	}

}
