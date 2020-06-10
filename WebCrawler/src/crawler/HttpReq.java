package crawler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class HttpReq {
	static int incercari = 0;
	public String cerereHttp(String cale, String domeniu, int port) throws UnknownHostException, IOException {
		String cerereHTTP = "GET /"+cale+ " HTTP/1.1\r\nHost: "+domeniu+"\r\nUser-Agent: CLIENT RIW\r\nConnection: close\r\n\r\n";
		InetAddress intAddress = InetAddress.getByName(domeniu);
		String ip = intAddress.getHostAddress();
		Socket client = new Socket(ip,port);
		DataOutputStream request = new DataOutputStream(client.getOutputStream());
		BufferedReader response = new BufferedReader(new InputStreamReader(client.getInputStream()));
		request.writeBytes(cerereHTTP);
		String responseLine = response.readLine();
		System.out.println(responseLine);
		
		boolean is200 = false;
		boolean is301 = false;
		boolean is302 = false;
		if(responseLine.contains("200 OK"))
		{
			is200 = true;
		}
		else if(responseLine.contains("301"))
		{
			is301 = true;
		}
		else if(responseLine.contains("302"))
		{
			is302 = true;
		}
		else
		{
			client.close();
			return null;
		}
		
		String newLocation = "";
		while ((responseLine = response.readLine()) != null)
        {
			StringBuilder ss = new StringBuilder();
			ss.append(responseLine);
            if (responseLine.equals(""))
            	break;
            if (responseLine.startsWith("Location:"))
            {
                newLocation = responseLine.replace("Location: ", "");
            }
            ss.toString();
        }
		
		
		String htmlPath;
		if(is200){
			StringBuilder sb = new StringBuilder();
			while((responseLine = response.readLine()) != null){
				sb.append(responseLine + "\n");
			}
			
			htmlPath =  domeniu + "/" +cale;
			if(!(htmlPath.endsWith(".html") || htmlPath.endsWith(".htm")) && !htmlPath.equals("/robots.txt")){
				if(!htmlPath.endsWith("/")){
					htmlPath += "/";
				}
				htmlPath += "index.html";
			}
			File file = new File(htmlPath);
			System.out.println(htmlPath);
            File dir = file.getParentFile();
            System.out.println(dir);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(htmlPath));
            
            writer.write(sb.toString());
            writer.close();
            client.close();
            return sb.toString();
		}
		else if(is301)
		{
			if(incercari < 3)
			{
				System.out.println(incercari);
				incercari++;
				client.close();
				URL urlNou = new URL(newLocation);
				String caleNoua = urlNou.getPath();
				String domeniuNou = urlNou.getHost();
				int portNou = urlNou.getPort();
				if(portNou == -1)
				{
					portNou = 80;
				}
				return cerereHttp(caleNoua,domeniuNou,portNou);
			}
			else
			{
				StringBuilder s = new StringBuilder();
				while((responseLine = response.readLine()) != null){
					s.append(responseLine + "\n");
				}
				BufferedWriter w = new BufferedWriter(new FileWriter("codEroare.txt"));
				w.write(s.toString());
				w.close();
				client.close();
				System.out.println("Cod 301. Numar de incercari epuizate.\n");
				return null;
			}
		}
		else if(is302)
		{
			if(incercari < 3)
			{
				incercari++;
				client.close();
				URL urlNou = new URL(newLocation);
				System.out.println(newLocation);
				String caleNoua = urlNou.getPath();
				String domeniuNou = urlNou.getHost();
				int portNou = urlNou.getPort();
				if(portNou == -1)
				{
					portNou = 80;
				}
				return cerereHttp(caleNoua,domeniuNou,portNou);
			}
			else
			{
				StringBuilder s = new StringBuilder();
				while((responseLine = response.readLine()) != null){
					s.append(responseLine + "\n");
				}
				BufferedWriter w = new BufferedWriter(new FileWriter("codEroare.txt"));
				w.write(s.toString());
				w.close();
				client.close();
				System.out.println("Cod 302. Numar de incercari epuizate.\n");
				return null;
			}
			
		}
		else
		{
			StringBuilder s = new StringBuilder();
			while((responseLine = response.readLine()) != null){
				s.append(responseLine + "\n");
			}
			BufferedWriter w = new BufferedWriter(new FileWriter("codEroare.txt"));
			w.write(s.toString());
			w.close();
			client.close();
			System.out.println("Cod 4xx\n");
			return null;
		}
	}
}
