package other;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class WordsTemplate {
	String word;
	HashMap<String, Integer> docs;
	
	public WordsTemplate(){
		word = "";
		docs = new HashMap<String, Integer>();
	}
	public WordsTemplate(String f, HashMap<String, Integer> w){
		word = f;
		docs = w;
	}

	public void setWord(String f){
		word = f;
	}
	
	public String getWord(){
		return word;
	}
	
	public void setDocs(HashMap<String, Integer> w){
		docs = w;
	}
	
	public HashMap<String, Integer> getDocs(){
		return docs;
	}
	public HashSet<String> getOnlyDocs(){
		HashSet<String> d = new HashSet<>();
		Iterator<Entry<String, Integer>> it = docs.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Integer> pair = (Map.Entry<String,Integer>)it.next();
	        d.add(pair.getKey());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		return d;
	}
	public void add(String fileName, Integer no){
		docs.put(fileName, no);
	}
}

