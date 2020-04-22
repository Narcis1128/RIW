package parser;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Extragere {
	public void parse(String path)
	{
		File file = new File(path);
		try {
			Document doc = Jsoup.parse(file, null);
			String title  = doc.title();
			if(doc.title() != null)
			{
				System.out.println("Titlu: " + title);
			}
			Elements metaTags = doc.getElementsByTag("meta");
			Elements aTags = doc.select("a[href]");
			String text =  doc.body().text();
			
			for(Element tag : metaTags)
			{
				String name = tag.attr("name");
				if(name.equals("description") || name.equals("keyword") || name.equals("robots"))
				{
					System.out.println("Numele: " + name);
				}
			}
			
			for(Element atag : aTags)
			{
				if(atag.attr("abs:href") != "") {
					System.out.println("Href: " + atag.attr("abs:href"));
				}
			}
			
			System.out.println("The text: " + text);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
