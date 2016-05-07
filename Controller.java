import java.util.*;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String crawlStorageFolder = "/data/crawl";
		int numberOfCrawlers = 1;
		
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxPagesToFetch(5000);
		config.setMaxDepthOfCrawling(5);
		config.setPolitenessDelay(500);
		
		config.setIncludeBinaryContentInCrawling(true);
		//config.setUserAgentString(userAgentString);
		
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,pageFetcher );
		CrawlController controller = new CrawlController(config,pageFetcher,robotstxtServer);
		
		controller.addSeed("http://www.marshall.usc.edu/");
		controller.start(MyCrawler.class, numberOfCrawlers);
		System.out.println("All urls processed:"+MyCrawler.URL_Count);
		System.out.println("All unique urls processed:"+MyCrawler.hs.size());
		System.out.println("unique School urls processed:"+MyCrawler.hs_uniqueSchool.size());
		System.out.println("unique USC urls processed:"+MyCrawler.hs_uniqueUSC.size());
		System.out.println("other unique urls processed:"+MyCrawler.hs_unique.size());
		Set set = MyCrawler.HM_Status.entrySet();
	    Iterator i = set.iterator();
	    while(i.hasNext()) {
	         Map.Entry me = (Map.Entry)i.next();
	         System.out.print(me.getKey() + ": ");
	         System.out.println(me.getValue());
	      }
	    set=MyCrawler.HM_CT.entrySet();
	    i = set.iterator();
	    while(i.hasNext()) {
	         Map.Entry me = (Map.Entry)i.next();
	         System.out.print(me.getKey() + ": ");
	         System.out.println(me.getValue());
	      }
	    System.out.println("F1:"+MyCrawler.F1);
	    System.out.println("F2:"+MyCrawler.F2);
	    System.out.println("F3:"+MyCrawler.F3);
	    System.out.println("F4:"+MyCrawler.F4);
	    
	    System.out.println("Size < 1kb:"+MyCrawler.size1);
	    System.out.println("Size 1kb~10kb:"+MyCrawler.size2);
	    System.out.println("Size 10kb~100kb:"+MyCrawler.size3);
	    System.out.println("Size 100kb~1mb:"+MyCrawler.size4);
	    System.out.println("Size > 1mb:"+MyCrawler.size5);
	    
	}

}
