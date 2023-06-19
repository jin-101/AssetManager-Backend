package com.shinhan.assetManager.stock;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.shinhan.assetManager.mongo.MongoDbFactory;

public class Test {
	
	public static void main(String[] args) {
		MongoDatabase db =  MongoDbFactory.getDatabase();
		MongoCollection<Document> kospi = db.getCollection("kospi");
		
//		Bson filter = new Document("Data",new Document("$elemMatch",new Document("corpCode","005930")));
//		Bson project = new Document("Data",new Document("$elemMatch",new Document("corpCode","005930"))).append("Day", 1L);
		Bson sort = new Document("Day",-1L);
		
//		Document doc   =  kospi.find(filter).projection(project).sort(sort).first();
//		List<Document> data = doc.getList("Data", Document.class);
//		Document stockData  = data.get(0);
//		System.out.println(stockData.get("corpName"));
		
		Document doc = kospi.find().sort(sort).first();
		List<Document> data = doc.getList("Data", Document.class);
		
		for(int i=0;i<data.size();i++) {
			Document stock = data.get(i);
			
			if(stock.get("corpCode").equals("005930")) {
				System.out.println(stock.get("corpName"));
			}
			
			
			
		}
		
		

		
	}

}
