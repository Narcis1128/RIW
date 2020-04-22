package parser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import other.DirectIndexJsonTemplate;
import other.WordsTemplate;
public class IndirectIndex {
	//read list of filetemplates from json
	
	//reverse
	private static List<WordsTemplate> reverse(String filename){
		List<WordsTemplate> list = new ArrayList<>();

        Gson gson = new Gson();
        
        String json = Parser.readFromFile(filename);
        List<DirectIndexJsonTemplate> listOfFiles = gson.fromJson(json, new TypeToken<List<DirectIndexJsonTemplate>>() {}.getType());
        
        for (DirectIndexJsonTemplate di : listOfFiles) {
			for (Map.Entry<String,Integer> word : di.getWords().entrySet()) {
				WordsTemplate wt = new WordsTemplate();
				wt.setWord(word.getKey());
				wt.add(di.getFileName(), word.getValue());
				list.add(wt);
			}
		}
        return list;
	}
	//sort
	private static void sort(List<WordsTemplate> list) {
        list.sort(Comparator.comparing(WordsTemplate::getWord));
    }
	//reuniune
	private static List<WordsTemplate> gather(List<WordsTemplate> list){
		List<WordsTemplate> finalList = new ArrayList<>();
		finalList.add(list.get(0));
		int last = 0;
		for (WordsTemplate wordsTemplate : list) {
			if(finalList.get(last).getWord().equals(wordsTemplate.getWord())){
				finalList.get(last).getDocs().putAll(wordsTemplate.getDocs());;
			}
			else{
				finalList.add(wordsTemplate);
				++last;
			}
		}
		return finalList;
	}
	//scriere in fisier
	public static void indirectIndexToFile(List<WordsTemplate> list){
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		String strJson = g.toJson(list);
		Writer writer = null;
		try {
			writer = new FileWriter("indexIndirect.json");
			writer.write(strJson);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static List<WordsTemplate> indirectIndex(){
		List<WordsTemplate> list = IndirectIndex.reverse("indexDirect.json");
		
		sort(list);
		
		List<WordsTemplate> finalList = gather(list);
		
		indirectIndexToFile(finalList);
		return finalList;
	}
}
