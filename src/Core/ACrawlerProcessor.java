package Core;



import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Map;
import Core.Interfaces.ICrawler;


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
	
	public IStatisticsDumper GetDumper()
	{
		return null;
	}
		
	public CrawlerRunner[] ExcuteCrawler(IElement headElement,ECrawlingType crawlType, long maxExcution)
	{
		long leftUserToCrawl = QueueCrawlinTargets.GetInstance().NumbertOfTargets();
		WriteLineToLog("NumbertOfTargets = " + leftUserToCrawl, ELogLevel.INFORMATION);
		long crawled = 0;
		IStatisticsDumper  dumper = GetDumper(); 
		while (leftUserToCrawl > 0 && crawled < maxExcution)
		{
			if (CrawlTopUserTarget(headElement,crawlType)!=null|| CoreContext.MAX_RUNNERS == 1 )
			{
				if(crawled%500 == 0 && dumper != null && crawled >1) //TODO change it to ~150 users 
				{//add mutex 
					if( !dumper.DumpAllTargetsStatData())
					{
						String errMsg = "failed to dump all users statistic Data";
						WriteLineToLog(errMsg, ELogLevel.ERROR);
						WriteToConsole(errMsg);
					}
				}
				
				if (crawled % 50 == 0 && crawled!=0)
				{
					WriteToConsole("crawling proceed : " + crawled +  "objects have been crawled !!");
					WriteToConsole( QueueCrawlinTargets.GetInstance().NumbertOfTargets() +" objects are waiting to be crawl...");
				}
				
				WriteLineToLog("count ="+crawled,ELogLevel.INFORMATION);
				if (CoreContext.SET_GRAPH && crawled %100 == 0) //TODO need to change this 
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
	
	
	
	private ThreadInfo[]  FindDeadLockThreads() // find if there is deadlock !
	{
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		long[] threadIds = bean.findDeadlockedThreads();
		if (threadIds == null) return null;
		
		return 	bean.getThreadInfo(threadIds);
		
	}

	public CrawlerRunner CrawlTopUserTarget(IElement headElement ,ECrawlingType crawlType)
	{
		CrawlerRunner runner = null;
		int targetNum =0;	
	//	WriteLineToLog("activeCount= "+CrawlerRunner.activeCount(),ELogLevel.INFORMATION);
		//WriteLineToLog("users left to crawled=" + QueueCrawlinTargets.GetInstance().NumbertOfTargets(), ELogLevel.INFORMATION);
		if (m_maxThread == 1)
		{
			String NextTarget =  QueueCrawlinTargets.GetInstance().GetNextTarget();
			if (NextTarget !="" && NextTarget != null )	//TODO::why this again...
			{ 
				String msg = ++targetNum +  ") crawl target : " +NextTarget ; 
				WriteToConsole(msg);
				WriteToLog(msg, ELogLevel.INFORMATION);
				Object[] params = {NextTarget};
				ICrawler crawler  = m_crawlerFactory.GenerateCrawler(crawlType, params);
				//IElement userElm = new UserCrawler(userName).Crawl(m_depthbehavior.get(ECrawlingType.User));
				IElement elm = crawler.Crawl(m_depthbehavior.get(crawlType));
				if (elm != null)
				{
					elm.Serialize(); 
					//headElement.AddElement(elm); //for improve performance 
					msg= "finish serialize " +elm.GetName() + " elemnet!" ;
					WriteToConsole(msg);
					WriteLineToLog(msg, ELogLevel.INFORMATION);
					return runner;
				}
				WriteLineToLog("failed to create user element",ELogLevel.ERROR);
			}
			return runner;
		}
		
		if(CrawlerRunner.activeCount() <= m_maxThread)
		{
			ThreadInfo[] threadsData  = FindDeadLockThreads();
			if (threadsData != null)
			{
				for(ThreadInfo ti :threadsData )
				{
					String msg = "need to kill this thread... thread in deadlock " + ti.toString();
					WriteToConsole(msg); 
					WriteLineToLog(msg, ELogLevel.ERROR);
				}
			}
			else
			{
				String NextTarget =  QueueCrawlinTargets.GetInstance().GetNextTarget();
				if (NextTarget != "" && NextTarget != null)
				{
					Object[] params = {NextTarget};
					runner = new CrawlerRunner(m_crawlerFactory.GenerateCrawler(crawlType, params), m_depthbehavior.get(crawlType),new String(NextTarget));
					runner.SetHeadElement(headElement);
					String runMsg = " execute Runner:# " + runner.getId()+" for Target="+NextTarget;
					WriteLineToLog(runMsg, ELogLevel.INFORMATION);
					WriteToConsole(runMsg);
					CrawledCount++;
					runner.start();
				}
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
