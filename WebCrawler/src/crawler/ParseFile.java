package crawler;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseFile {
	String file;
	Document doc;
	ParseFile(String f)
	{
		file = f;
		doc = Jsoup.parse(file);
	}
	
	public String metaRobots() {
		Element robots = doc.selectFirst("meta[name=robots]");
		String ret = "";
		if(robots == null)
		{
			System.out.println("Nu exista tag-ul meta cu name = robots.");
		}
		else
		{
			ret = robots.attr("content");
		}
		return ret;
	}
	
	public Set<String> getLinks(String url){
		//System.out.println(url);
		Document doc1 = null;
		try {
			doc1 = Jsoup.connect(url).get();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Elements links = doc1.select("a[href]");
		Set<String> urls = new HashSet<String>();
		for (Element link : links) {
            String absoluteLink = link.absUrl("href");
            //System.out.print(absoluteLink);
            int anchorPosition = absoluteLink.indexOf('#');
            if (anchorPosition != -1)
            {
                StringBuilder tempLink = new StringBuilder(absoluteLink);
                tempLink.replace(anchorPosition, tempLink.length(), "");
                absoluteLink = tempLink.toString();
            }
            try {
		            URL absoluteLinkURL = new URL(absoluteLink);
		            String path = absoluteLinkURL.getPath();
		            String extension = path.substring(path.lastIndexOf(".")+ 1);
            		if (!extension.isEmpty())
                    {
                       
                        if (!(path.endsWith("html") || path.endsWith("htm")))
                        {
                            continue;
                        }
                    }
            		//System.out.println(absoluteLink);
            		urls.add(absoluteLink);
            }catch(Exception e)
            {
            	continue;
            }
		}
		return urls;
	}
	
	public String getText(){
		return doc.body().text();
	}
	
    public Document getDoc()
    {
        return doc;
    }
}
