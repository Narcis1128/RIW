package other;

import java.util.HashMap;

public class DirectIndexJsonTemplate {
	String fileName;
	HashMap<String, Integer> words;
	
	public DirectIndexJsonTemplate(){
		fileName = null;
		words = null;
	}
	public DirectIndexJsonTemplate(String f, HashMap<String, Integer> w){
		fileName = f;
		words = w;
	}
	
	public void setFileName(String f){
		fileName = f;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public void setWords(HashMap<String, Integer> w){
		words = w;
	}
	
	public HashMap<String, Integer> getWords(){
		return words;
	}
	@Override
	public boolean equals(Object o){
		if (o == null) return false;
	    if (!(o instanceof DirectIndexJsonTemplate))
	        return false;
	    if (o == this)
	        return true;
	    if(this.getFileName() == ((DirectIndexJsonTemplate) o).getFileName() && this.getWords().equals(((DirectIndexJsonTemplate) o).getWords())){
	    	return true;
	    }
	    return false;
	}
}
