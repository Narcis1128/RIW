package mongo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import other.WordsTemplate;
import other.DirectIndexJsonTemplate;
public class MongoDB {
	static MongoClient mongoClient = new MongoClient("localhost", 27017);
	static MongoDatabase database = mongoClient.getDatabase("riw");
	
	public void populateCollectionDirect(List<DirectIndexJsonTemplate> listOfFileTemplates) {
		MongoCollection<Document> collection = database.getCollection("indexDirect");

		for(DirectIndexJsonTemplate directIndex : listOfFileTemplates)
		{
			Document doc = new Document();
			String word=directIndex.getFileName();
			
			doc.append("fileName",word);
			List<Document> docs =  new ArrayList<Document>();
			for(Map.Entry<String, Integer> entry : directIndex.getWords().entrySet())
			{
				Document docIterator = new Document();
				docIterator.append("word", entry.getKey());
				docIterator.append("count", entry.getValue());
				docs.add(docIterator);
			}
			doc.append("docs",docs);
			collection.insertOne(doc);
		}
	}
	
	public void populateCollectionIndirect(List<WordsTemplate> finalList)
	{
		MongoCollection<Document> collection = database.getCollection("indexIndirect");
		
		for(WordsTemplate wordFormat : finalList)
		{
			Document doc = new Document();
			String word=wordFormat.getWord();
			
			doc.append("word",word);
			List<Document> docs =  new ArrayList<Document>();
			for(Map.Entry<String, Integer> entry : wordFormat.getDocs().entrySet())
			{
				Document docIterator = new Document();
				docIterator.append("doc", entry.getKey());
				docIterator.append("count", entry.getValue());
				docs.add(docIterator);
			}
			doc.append("docs",docs);
			collection.insertOne(doc);
		}
	}
}
