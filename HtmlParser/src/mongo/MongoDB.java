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
	
	/*public List<WordsTemplate> getDataFromIndirect()
	{
	    MongoCollection <Document> collection = database.getCollection("indexIndirect");
	    FindIterable<Document> documents = collection.find();
	    
	    List<WordsTemplate> list = new ArrayList<WordsTemplate>();
	    for (Document document : documents) {
	    	WordsTemplate wf = makeList(document);
	    	list.add(wf);
	    }
	    return list;
	}
	
	public WordsTemplate makeList(Document document)
	{
		WordsTemplate wf = new WordsTemplate();
		HashMap<String, Integer> docs = new HashMap<String, Integer>();
		wf.setWord(document.getString("word"));
		List<Document> docTerms =  (List<Document>)document.get("docs");
    	for (Document docTerm : docTerms) {
    		docs.put(docTerm.getString("doc"), docTerm.getInteger("count"));
    	}
    	wf.setDocs(docs);
		return wf;
	}*/
	
	/*public List<DirectIndexJsonTemplate> getDataFromDirect()
	{
	    MongoCollection <Document> collection = database.getCollection("indexDirect");
	    FindIterable<Document> documents = collection.find();
	    
	    List<DirectIndexJsonTemplate> list = new ArrayList<DirectIndexJsonTemplate>();
	    for (Document document : documents) {
	    	DirectIndexJsonTemplate wf = makeListDirect(document);
	    	list.add(wf);
	    }
	    return list;
	}
	
	public DirectIndexJsonTemplate makeListDirect(Document document)
	{
		DirectIndexJsonTemplate wf = new DirectIndexJsonTemplate();
		HashMap<String, Integer> words = new HashMap<String, Integer>();
		wf.setFileName(document.getString("fileName"));
		List<Document> docTerms =  (List<Document>)document.get("docs");
    	for (Document docTerm : docTerms) {
    		words.put(docTerm.getString("word"), docTerm.getInteger("count"));
    	}
    	wf.setWords(words);
		return wf;
	}*/
}
