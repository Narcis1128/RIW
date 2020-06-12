package crawler;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Crawler{
	LinkedList<String> coada;
	HashSet<String> vizitate;
	HashSet<String> domeniuVizitate;
	HttpReq http;
	Crawler(){
		coada = new LinkedList<String>();
		vizitate = new HashSet<String>();
		domeniuVizitate = new HashSet<String>();
		http = new HttpReq();
	}
	
	public void crawler(ArrayList<String> urls, int limita)
	{
		int nrUrl = 0;
		coada.addAll(urls);
		while((coada.isEmpty()!= true) && (nrUrl<limita)) {
			String urlCoada = coada.pop();
			URL url;
			try {
				url= new URL(urlCoada);
				String cale = url.getPath();
				String domeniu = url.getHost();
				int port = url.getPort();
				if(port == -1)
				{
					port = 80;
				}
				System.out.println(cale + "  " + domeniu + " " + port);
			if (!url.getProtocol().equals("http")) 
            {
                System.out.println("Nu e HTTP URL: " + urlCoada);
                vizitate.add(urlCoada);
                continue;
            }
			if(vizitate.isEmpty()!=true)
			{
				if(vizitate.contains(urlCoada))
				{
					continue;
				}
				else
				{
					if(!domeniuVizitate.contains(domeniu))
					{
						String robot = http.cerereHttp("/robots.txt", domeniu, port);
						domeniuVizitate.add(domeniu);
						if(robot != null)
						{
							byte[] encoded = Files.readAllBytes(Paths.get(robot));
							String robotText = new String(encoded);
							if(!RobotParser.isAllowed(url, robotText))
							{
								System.out.print("Interzis pentru explorare\n");
								continue;
							}
						}
					}
					String save = http.cerereHttp(cale,domeniu,port);
					vizitate.add(urlCoada);
					if(save != null)
					{
						ParseFile pf = new ParseFile(save);
						String robots = pf.metaRobots();
						if(robots.equals(""))
						{
							if(robots == "all" || robots =="index")
							{
								String text = pf.getText();
								BufferedWriter w = new BufferedWriter(new FileWriter(pf.getDoc().location()+".txt"));
								w.write(text);
								w.close();
							}
							if(robots == "all" || robots =="follow")
							{
								Set<String> links = pf.getLinks(urlCoada);
								coada.addAll(links);
							}
						}
						//Set<String> links = pf.getLinks(urlCoada);
						//coada.addAll(links);
						nrUrl++;
					}
					else {
						nrUrl++;
						continue;
					}
				}
			}
			else
			{
				String robot = http.cerereHttp("/robots.txt", domeniu, port);
				domeniuVizitate.add(domeniu);
				System.out.println("ajung");
				if(robot != null)
				{
					String robotText = new String(robot);
					if(!RobotParser.isAllowed(url, robotText))
					{
						System.out.print("Interzis pentru explorare\n");
						continue;
					}
				}
				String save = http.cerereHttp(cale,domeniu,port);
				vizitate.add(urlCoada);
				if(save != null)
				{
					ParseFile pf = new ParseFile(save);
					String robots = pf.metaRobots();
					if(robots.equals(""))
					{
						if(robots == "all" || robots =="index")
						{
							String text = pf.getText();
							BufferedWriter w = new BufferedWriter(new FileWriter(pf.getDoc().location()+".txt"));
							w.write(text);
							w.close();
						}
						if(robots == "all" || robots =="follow")
						{
							Set<String> links = pf.getLinks(urlCoada);
							coada.addAll(links);
							
						}
					}
					Set<String> links = pf.getLinks(urlCoada);
					coada.addAll(links);
					nrUrl++;
				}
				else {
					nrUrl++;
					continue;
				}
			}
			
			System.out.println(nrUrl);
			}catch(IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	void getLinks(String url, LinkedList<String> urls) {

	    if (urls.contains(url)) {
	        return;
	    }
	    urls.add(url);

	    try {
	        Document doc = Jsoup.connect(url).get();
	        Elements elements = doc.select("a");
	        System.out.println(elements);
	        for(Element element : elements){
	            System.out.println(element.absUrl("href"));
	            getLinks(element.absUrl("href"), urls);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	

}