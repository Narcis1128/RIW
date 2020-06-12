package crawler;

import java.util.ArrayList;

public class Head {

	public static void main(String[] args) {
		Crawler crawl = new Crawler();
		ArrayList<String> links = new ArrayList<String>();
		links.add("http://riweb.tibeica.com/crawl/");
		long start = System.nanoTime();
		crawl.crawler(links, 100);
		long elapsedTime = System.nanoTime() - start;
		System.out.println((double)elapsedTime/1_000_000_000.0);
	}

}
