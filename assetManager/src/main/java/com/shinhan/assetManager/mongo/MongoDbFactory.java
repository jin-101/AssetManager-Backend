package com.shinhan.assetManager.mongo;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDbFactory {
	
	private static String uri;
	private static MongoClient mongoClinet;
	private static MongoDatabase database;
	
	static {
		try {
			uri = getPw();
			mongoClinet = MongoClients.create(uri);
			database = mongoClinet.getDatabase("StockKr");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static MongoDatabase getDatabase() {
		return database;
	}


	private static String getPw() throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook("C://Mongo/pw.xlsx");
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		XSSFRow rowWithPw = sheet.getRow(0);
		String pw = rowWithPw.getCell(0).toString();
		
		
		return pw;
	}
	

}
