package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import other.Metadata;
import other.Stemmer;

public final class Data {
	private static File exceptions;
	private static HashSet<String> exception;
	private static File stop;
	private static HashSet<String> stopwords;
	public static void inIt() {
		exceptions = new File("exceptii.txt");
		exception = new HashSet<>();
		stop = new File ("stop_words.txt");
		stopwords = new HashSet<>();
		BufferedReader br = null;
		FileReader fr = null;

		try {

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(exceptions);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				exception.add(sCurrentLine);
			}

			fr = new FileReader(stop);
			br = new BufferedReader(fr);
			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				stopwords.add(sCurrentLine);
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
	public static String getTitle(Document doc ){
		if(!doc.title().isEmpty()){
			return doc.title() + "\n";
		}
		else{
			return null;
		}
	}
	public static List<Metadata> getMetadatas(Document doc){
		List<Metadata> metadatas = new ArrayList<Metadata>();
		Elements metaTags = doc.select("meta");
		for (Element metatag : metaTags) 
		{
			if(metatag.hasAttr("name")) {
				String name = metatag.attr("name").toLowerCase();
				if( name.equals("keywords") || name.equals("description") || name.equals("robots")){
					metadatas.add( new Metadata(name,metatag.attr("content")));
				}
			}
		}
		return metadatas;
	}
	public static HashSet<String> getLinks(Document doc){
		HashSet<String> urls = new HashSet<>();
		Elements href = doc.select("a");
		for (Element hre : href)
		{
			String URL = hre.attr("abs:href");
			if(URL.contains("#")){
				int index = URL.indexOf("#");
				if(URL.substring(0,index).length() != 0){
					urls.add(URL.substring(0, index));
				}

			}
			else{
				urls.add(URL);
			}
		}
		return urls;
	}
	public static String getText(Document doc){
		return doc.body().text();
	}
	public static boolean isException(String word){
		return exception.contains(word);
	}
	public static boolean isStopWord(String word){
		return stopwords.contains(word);
	}
	public static void getWords(String text, HashMap<String,Integer> words){
		inIt();
		char c;
		String word = "";
		int len = text.length();


		for(int i = 0; i < len; i++){
			c = text.charAt(i);
			if((c<'a'|| c>'z') && ( c<'A' || c>'Z') && (c<'0' || c>'9')){
				if(word != ""){

					if(isException(word)){

						if(words.containsKey(word)){
							word = word.toLowerCase();
							words.put(word, words.get(word) + 1);

						}
						else{
							word = word.toLowerCase();
							words.put(word, 1);
						}
						word="";
					}
					else {
						word = word.toLowerCase();
						if(isStopWord(word)){
							word="";
						}
						else{
							Stemmer s = new Stemmer();
							char[] chs = word.toCharArray();
							s.add(chs, chs.length);
							s.stem();
							word = s.toString();
							if(words.containsKey(word)){
								words.put(word, words.get(word) + 1);

							}
							else{
								words.put(word, 1);
							}
							word="";
						}
					}


				}
			}
			else{
				word += c;
			}
		}
	}
}
