package TratisFinderTests;

import static org.junit.Assert.*;

import org.junit.Test;

import Services.Log.ELogLevel;

import Core.QueueCrawlinTargets;
import Core.Interfaces.ICrawlingTargets;

public class CrawlerTargetsTest extends test {
	
	final int THREAD_TO_RUN =30;
	
	
	
	@Test
	public void CrawlingTargetsTest() {
		ICrawlingTargets pUsersCrawlingTargets = QueueCrawlinTargets.GetInstance();
		pUsersCrawlingTargets.AddTarget("Amit");
		pUsersCrawlingTargets.AddTarget("Oz");
		pUsersCrawlingTargets.AddTarget("Tsvika");
		String NextTarget = pUsersCrawlingTargets.GetNextTarget();
		if (NextTarget!=null)
		{
			assertTrue(NextTarget.compareTo("Amit")==0 || NextTarget.compareTo("Oz")==0 || NextTarget.compareTo("Tsvika")==0);
			NextTarget = pUsersCrawlingTargets.GetNextTarget();
		}
		if (NextTarget!=null)
		{
			assertTrue(NextTarget.compareTo("Amit")==0 || NextTarget.compareTo("Oz")==0 || NextTarget.compareTo("Tsvika")==0);
			NextTarget = pUsersCrawlingTargets.GetNextTarget();
		}
		if (NextTarget!=null)
		{
			assertTrue(NextTarget.compareTo("Amit")==0 || NextTarget.compareTo("Oz")==0 || NextTarget.compareTo("Tsvika")==0);
			NextTarget = pUsersCrawlingTargets.GetNextTarget();
		}
		//assertTrue(NextTarget==null);
		
	}
	
	@Test
	public void ThreadQueueCrawlingTargetTest()
	{
		int numofThread = Thread.activeCount();
		for (int i = 0; i<THREAD_TO_RUN ;i++)
		{
			Thread t1  = new Thread(new Runnable() {
									
				@Override
				public void run() {
					CrawlingTargetsTest();
					try
					{
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						WriteLineToLog("excption in targets test msg="+e.getMessage(), ELogLevel.ERROR);
					}
				}
			});
			t1.start();
			
		}
		while (Thread.activeCount()>numofThread)
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				WriteLineToLog("excption in targets test waiting.. msg="+e.getMessage(), ELogLevel.ERROR);
			}
		}
	}
}
