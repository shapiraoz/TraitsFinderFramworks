package TratisFinderTests;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Core.ACrawlerProcessor;

//import Core.Crawlers.PintersetCrawler;


public class CrawlerProccessorTest extends test{

	private final int NUMBER_OF_THEARD_FOR_TEST =  5;
	
	@Before
	public void setUp() throws Exception 
	{
		ACrawlerProcessor.CrawledCount = 0;
		super.setUp();		
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testMultiThreaded()
	{
		/*
		//String[] locks = CrawlerProccessor.GetInstance().getLocks();
		ICrawler crawler  = new  PintersetCrawler();
		IElement mainElem =  crawler.Crawl(ACrawlerProcessor.GetInstance().GetDepthCrawling(ECrawlingType.Main));
		assertTrue(mainElem.GetElements().size()==0);
		CrawlerRunner[] runners =  ACrawlerProcessor.GetInstance().ExcuteCrawler(mainElem,NUMBER_OF_THEARD);
		
		//need to fix it ....
		
		for (int i =0 ; i< runners.length ; i++)
		{
			synchronized(runners[i].Getlock())
			{
				
					try {
						runners[i].Getlock().wait();//
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						WriteLineToLog("exception happen msg="+e.getMessage(), ELogLevel.ERROR);
					}
			}
		}
		*/
	}
	
	
	@Test
	public void testSingelThread() 
	{
		/*
		ICrawler crawler  = new  PintersetCrawler();
		IElement mainElem =  crawler.Crawl(ACrawlerProcessor.GetInstance().GetDepthCrawling(ECrawlingType.Main));
		assertTrue(mainElem.GetElements().size()==0);
		CrawlerRunner runner = ACrawlerProcessor.GetInstance().CrawlTopUserTarget(mainElem);
		synchronized(runner.Getlock())
		{
			try {
				runner.Getlock().wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					WriteLineToLog("exception happen msg="+e.getMessage(), ELogLevel.ERROR);
				}
		}
		assertTrue(mainElem.GetElements().size()>0);
		*/
	}

	
	
}
