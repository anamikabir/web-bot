//import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

//import java.util.regex.Pattern;
import java.util.*;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	public static int size1=0,size2=0,size3=0,size4=0,size5=0;
	public static int F1=0,F2=0,F3=0,F4=0;
	public static int URL_Count=0;
//	private final static Pattern FILTERS= Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|zip|gz))$");
	public static HashSet<String> hs = new HashSet<String>();
	public static HashSet<String> hs_unique = new HashSet<String>();
	public static HashSet<String> hs_uniqueUSC = new HashSet<String>();
	public static HashSet<String> hs_uniqueSchool = new HashSet<String>();
	public static HashMap HM_Status = new HashMap();
	public static HashMap HM_CT = new HashMap();
	@Override 
	public boolean shouldVisit (Page referringPage,WebURL url) 
			{		URL_Count++;
					hs.add(url.getURL().toLowerCase());
				try{
					String s1="";
					if(url.getSubDomain().toLowerCase().contains("marshall") && url.getDomain().toLowerCase().contains("usc.edu"))
					{
						s1="OK";
						hs_uniqueSchool.add(url.getURL().toLowerCase());
					}
					else if(url.getDomain().toLowerCase().contains("usc.edu"))
					{
						s1="USC";
						hs_uniqueUSC.add(url.getURL().toLowerCase());
					}
					else {
						s1="OutUSC";
						hs_unique.add(url.getURL().toLowerCase());
					}
					FileWriter fw_url = new FileWriter("urls.csv",true);
					StringBuilder sb1 = new StringBuilder();
					sb1.append(url.getURL());
					sb1.append(",");
					sb1.append(s1);
					sb1.append("\n");
					fw_url.append(sb1.toString());
					fw_url.close();
		
				}
				catch(Exception e)
				{e.printStackTrace();}
				String href = url.getURL().toLowerCase();
				return href.contains("marshall.usc.edu");
						
			}
	@Override
	public void visit(Page page)
	{
		
		String url = page.getWebURL().getURL();
		System.out.println("URL: "+url);
		int OutGoingLinks=0;
		if (page.getParseData() instanceof HtmlParseData)
		{
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text=htmlParseData.getText();
			String html=htmlParseData.getHtml();
			Set<WebURL> links =htmlParseData.getOutgoingUrls();
			try
			{
			FileWriter fw6 = new FileWriter("pagerank.csv",true);
			StringBuilder ss= new StringBuilder();
			ss.append(url);
			Iterator<WebURL> it= links.iterator();
			while(it.hasNext())
			{
				ss.append(",");
				ss.append(it.next().getURL());
			}
			ss.append("\n");
			fw6.append(ss.toString());
			fw6.close();
			
			}
			catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("Text Length: "+text.length());
			System.out.println("HTML Length: "+html.length());
			System.out.println("Number of outgoing links: "+links.size());
			OutGoingLinks=links.size();
		}
		String s4;
		if( page.getContentData().length < 1024)
		{
			s4=page.getContentData().length+" B";
		}
		else if ( page.getContentData().length > 1024 && page.getContentData().length < 1048576)
		{ s4=(int)(page.getContentData().length/1024)+" KB"; }
		else { s4=(int)(page.getContentData().length/1048576)+" MB"; }
		if(page.getContentData().length >=1048576){size5++;}
		else if (page.getContentData().length >102400){size4++;}
		else if (page.getContentData().length >10240) {size3++;}
		else if(page.getContentData().length >1024){size2++;}
		else {size1++;}
		try{
			String CT=page.getContentType();
			if(HM_CT.containsKey(CT))
			{
				int current = (int) HM_CT.get(CT);
				current+=1;
				HM_CT.put(CT,current);
			}
			else{	
			HM_CT.put(CT, 1);
			}
			FileWriter fw1 = new FileWriter("visit.csv",true);
			StringBuilder sb1 = new StringBuilder();
			sb1.append(url);
			sb1.append(",");
			sb1.append(s4);
			sb1.append(",");
			sb1.append(OutGoingLinks);
			sb1.append(",");
			sb1.append(page.getContentType());
			sb1.append("\n");
			fw1.append(sb1.toString());
			fw1.close();
			String fileExtn="";
			Boolean toDL=false;
			if(page.getContentType().contains("html")||page.getContentType().contains("htm"))
			{
				fileExtn="html";
				toDL=true;
			}
			else if(page.getContentType().contains("doc")||page.getContentType().contains("msword"))
			{	fileExtn="doc";
				toDL=true;
			}
			else if(page.getContentType().contains("pdf"))
			{
				fileExtn="pdf";
				toDL=true;
			}
			if(toDL)
			{
				String fileName = url.replace("/","_");
				fileName= fileName.replace(":","_");
				fileName= fileName.replace(".","_");
				fileName= fileName.replace("-","_");
				fileName= fileName.replace("?","_");
				fileName= fileName.replace("=","_");
				fileName= fileName.replace("%","_");
				fileName= fileName.replace("@","_");
				fileName= fileName.replace("&","_");
				fileName= fileName+"."+fileExtn;
				if(fileExtn=="html")
				{
				FileWriter fwurl = new FileWriter("UrlMap.csv",true);
				StringBuilder sb7 = new StringBuilder();
				sb7.append(url);
				sb7.append(",");
				sb7.append(fileName);
				sb7.append("\n");
				fwurl.append(sb7.toString());
				fwurl.close();
				}
				FileOutputStream fos = new FileOutputStream(fileName);
				fos.write(page.getContentData());
				fos.close();
			}
		}
		catch(Exception e){e.printStackTrace();}
	}
	@Override
	  protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) 
	{
		try
		{
			F1++;
			if(statusCode>=200 && statusCode<300 ){F2++;}
			else if(statusCode>=300 && statusCode<400 ){F3++;}
			else if(statusCode>=400){F4++;}
			FileWriter fw = new FileWriter("fetch.csv",true);
			StringBuilder sb = new StringBuilder();
			sb.append(webUrl.getURL());
			sb.append(",");
			sb.append(statusCode);
			sb.append("\n");
			fw.append(sb.toString());
			fw.close();
			if(HM_Status.containsKey(statusCode)){
				/*int current = (int) HM_Status.get(statusCode);
				
				current+=1;*/
					    
				HM_Status.put(statusCode,(int)HM_Status.get(statusCode)+1);

			}
			else{	
				HM_Status.put(statusCode, 1);}
		}
		catch(IOException e){e.printStackTrace();}
	  }
}
