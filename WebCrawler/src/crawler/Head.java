package crawler;

import java.util.ArrayList;
import java.util.LinkedList;

public class Head {

	public static void main(String[] args) {
		Crawler crawl = new Crawler();
		ArrayList<String> links = new ArrayList<String>();
		links.add("http://riweb.tibeica.com/crawl/");
		crawl.crawler(links, 100);
	}

}
