package Core;



import java.util.HashMap;

import java.util.Map;

import Core.Interfaces.ICrawler;
import Core.Interfaces.ICrawlerProcessor;

import Elements.IElement;
import Services.Log.ELogLevel;

public class ACrawlerProcessor extends CommonCBase 
{
	private static ACrawlerProcessor m_CrawlerProcessor;
	
	public static int CrawledCount; 
	protected int m_maxThread  = CoreContext.MAX_RUNNERS;

	
	protected IElement m_headElement ;
	protected Map <ECrawlingType,Boolean> m_depthbehavior ;
	protected ACrawlerFactory m_crawlerFactory;
	
	protected ACrawlerProcessor()
	{
		CrawledCount =0;
		Init();
		
	}

	private void Init()
	{
		
	}
	
		
	
	
	public static ACrawlerProcessor GetInstance()
	{
		if (m_CrawlerProcessor == null)
		{
			m_CrawlerProcessor = new ACrawlerProcessor();
		}
		return m_CrawlerProcessor;
	}

	/*@Override
	public boolean GetDepthCrawling(ECrawlingType crawlType) {
		
		return m_depthbehavior.get(crawlType);
	}
	*/
	
	
		
	public CrawlerRunner[] ExcuteCrawler(IElement headElement,ECrawlingType crawlType, long maxExcution)
	{
		long leftUserToCrawl = QueueCrawlinTargets.GetInstance().NumbertOfTargets();
		WriteLineToLog("NumbertOfTargets="+leftUserToCrawl, ELogLevel.INFORMATION);
		long crawled = 0;
		while (leftUserToCrawl > 0 && crawled <maxExcution)
		{
			if (CrawlTopUserTarget(headElement,crawlType)!=null)
			{
				if (crawled % 50 == 0 && crawled!=0)
				{
					
					WriteToConsole("crawling proceed : " + crawled + "objects have been crawled !!");
					WriteToConsole("queue exist with " + QueueCrawlinTargets.GetInstance().NumbertOfTargets() +" objects are waiting to crawl...");
				}
				WriteLineToLog("count ="+crawled,ELogLevel.INFORMATION);
				if (CoreContext.SET_GRAPH && crawled %100 == 0)
				{
					String msg = String.format("graph updated with %d objects",crawled);
					WriteLineToLog(msg,ELogLevel.INFORMATION);
					WriteToConsole(msg);
				}
				crawled++; 
			}
			
		}
		String msg = (crawled == maxExcution )? String.format("crawler end collecting %d users ", crawled) :
			(leftUserToCrawl == 0) ? "crawler don't have users to crawled , need to restart crawling" : "stop from unknow reason ";
		WriteToConsole(msg);
		WriteLineToLog(msg,ELogLevel.WARNING);
		return null;
	}
	

	public CrawlerRunner CrawlTopUserTarget(IElement headElement ,ECrawlingType crawlType)
	{
		CrawlerRunner runner = null;
			
	//	WriteLineToLog("activeCount= "+CrawlerRunner.activeCount(),ELogLevel.INFORMATION);
		if (m_maxThread == 1)
		{
			String NextTarget =  QueueCrawlinTargets.GetInstance().GetNextTarget();
			if (NextTarget !="" && NextTarget != null )	//TODO::why this again...
			{
				Object[] params = {NextTarget};
				ICrawler crawler  = m_crawlerFactory.GenerateCrawler(crawlType, params);
				//IElement userElm = new UserCrawler(userName).Crawl(m_depthbehavior.get(ECrawlingType.User));
				IElement elm = crawler.Crawl(m_depthbehavior.get(crawlType));
				if (elm != null)
				{
					if (CoreContext.SET_GRAPH) elm.Serialize();
					headElement.AddElement(elm);
					return runner;
				}
				WriteLineToLog("failed to create user element",ELogLevel.ERROR);
			}
			return runner;
		}
		
		if(CrawlerRunner.activeCount() <= m_maxThread)
		{
			String NextTarget =  QueueCrawlinTargets.GetInstance().GetNextTarget();
			if (NextTarget!=""&& NextTarget!=null)
			{
				Object[] params = {NextTarget};
				WriteLineToLog("users left to crawled=" + QueueCrawlinTargets.GetInstance().NumbertOfTargets(), ELogLevel.INFORMATION);
				runner = new CrawlerRunner(m_crawlerFactory.GenerateCrawler(crawlType, params), m_depthbehavior.get(crawlType),new String(NextTarget));
				runner.SetHeadElement(headElement);
				String runMsg ="execute Runner:# " + runner.getId()+" for Target="+NextTarget;
				WriteLineToLog(runMsg, ELogLevel.INFORMATION);
				WriteToConsole(runMsg);
				CrawledCount++;
				runner.start();
			}
				
		}
		return runner;
	}

	public boolean GetDepthCrawling(ECrawlingType crawlType)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
}
