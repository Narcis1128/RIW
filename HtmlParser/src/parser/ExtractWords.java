package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ExtractWords {
	String director;
	char[] punctuatie = {'\"', ',', '.', '!', '?', ':', ';'};
	public List<String> stop_words;
	List<String> exceptii;
	HashMap<String,Integer> hasTable;
	Queue<String> coadaDirector;
	List<String> numeFisiere;
	
	public ExtractWords(String dir) {
		coadaDirector = new LinkedList<String>();
		director = new String(dir);
		coadaDirector.add(director);
		hasTable = new HashMap<String, Integer>();
		stop_words = new ArrayList<String>();
		numeFisiere = new ArrayList<String>();
		BufferedReader buff = null;
		try {
			String c;
			buff = new BufferedReader (new FileReader("stop_words.txt"));
			while((c = buff.readLine()) != null) {
				stop_words.add(c);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(buff != null) {
					buff.close();
				}
			}catch(IOException ex) {
				ex.printStackTrace();
			}
			
		}
		exceptii = new ArrayList<String>();
		try {
			String c;
			buff = new BufferedReader (new FileReader("exceptii.txt"));
			while((c = buff.readLine()) != null) {
				stop_words.add(c);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(buff != null) {
					buff.close();
				}
			}catch(IOException ex) {
				ex.printStackTrace();
			}
			
		}
	}
	
	public void getFiles() {
		while(!coadaDirector.isEmpty())
		{
			File dir = new File(coadaDirector.element());
			coadaDirector.remove();
			File[] listaFisiere = dir.listFiles();
			for(File file : listaFisiere) {
				if(file.isFile()) {
					numeFisiere.add(file.getPath());
				}
				if(file.isDirectory()) {
					coadaDirector.add(file.getPath());
				}
					
			}
		}
	}
	
	public boolean isSemn(char semn) {
		for(char c : punctuatie) {
			if(c == semn) {
				return true;
			}
		}
		return false;
	}
	
	public void mapFile(String filename) {
		InputStream in = null;
		try {
			in = new FileInputStream(filename);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		Reader reader = null;
		try {
			reader = new InputStreamReader(in,"UTF-8");
		}catch(UnsupportedEncodingException ee) {
			ee.printStackTrace();
		}
		
		int r;
		StringBuilder sb = new StringBuilder();
		try {
			while((r = reader.read()) != -1) {
				char c = (char) r;
				if(Character.isWhitespace(c) || isSemn(c)) {
					if(sb.length() != 0) {
						String s = sb.toString();
						if(this.stop_words.indexOf(s) == -1) {
							int val = 0;
							if(hasTable.containsKey(s)) {
								val = hasTable.get(s);
							}
							hasTable.put(s, val + 1);
							sb = new StringBuilder();
						}
					}
				}
				else {
					sb.append(c);
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void map() {
		for(String file : numeFisiere) {
			mapFile(file);
		}
	}
}
