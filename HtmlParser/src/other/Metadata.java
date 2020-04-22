package other;

public class Metadata {
	private String name;
	private String content;
	public Metadata(String n, String c){
		name = n;
		content = c;
	}
	public void setName(String n){
		name = n;
	}
	public void setContent(String c){
		content = c;
	}
	public String getName(){
		return name;
	}
	public String getContent(){
		return content;
	}
	public String toString(){
		return "Name: " + name + "\nContent: " + content +"\n";
	}
}
