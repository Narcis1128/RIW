package parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import other.DirectIndexJsonTemplate;

public class DirectIndex {
	private static DirectIndexJsonTemplate getFileTemplate(String path){
		DirectIndexJsonTemplate di = new DirectIndexJsonTemplate();
		File file = new File(path);
		String filename = file.getName();
		String absolutePath = file.getAbsolutePath();
		String filePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
		//System.out.println(filename + absolutePath + " sau " + filePath +"/"+filename);
		File ff = new File(filePath+"/"+filename);
		HashMap<String, Integer> ww = Parser.parseText(ff);
		di.setFileName(absolutePath);
		di.setWords(ww);
		return di;
	}
	private static List<DirectIndexJsonTemplate> getListOfFileTemplates(List<String> paths){
		List<DirectIndexJsonTemplate> listOfFileTemplates = new ArrayList<>();
		for (String path : paths) {
			DirectIndexJsonTemplate di = DirectIndex.getFileTemplate(path);
			if(di.getFileName() != null && di.getWords() != null){
				listOfFileTemplates.add(di);
			}
		}
		return listOfFileTemplates;
	}
	public static void directIndexToFile(List<DirectIndexJsonTemplate> list){
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		String strJson = g.toJson(list);
		Writer writer = null;
		try {
			writer = new FileWriter("indexDirect.json");
			writer.write(strJson);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static List<DirectIndexJsonTemplate> directIndex(String dirName){
		List<String> paths = Parser.getFiles(new File(dirName));
		
		List<DirectIndexJsonTemplate> listOfFileTemplates = DirectIndex.getListOfFileTemplates(paths);
		directIndexToFile(listOfFileTemplates);
		return listOfFileTemplates;
	}
}
