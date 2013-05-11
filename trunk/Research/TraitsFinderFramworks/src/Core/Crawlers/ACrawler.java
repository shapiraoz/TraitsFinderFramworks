package Core.Crawlers;

import java.util.Random;

import Core.CommonCBase;
import Core.CoreContext;
import Services.FileServices;
import Services.ICollector;
import Services.WgetCollectorExecutors;
import Services.WgetCollector;
import Services.Log.ELogLevel;


public class ACrawler  extends CommonCBase
{
	protected ICollector m_collector;
	protected Random m_randomGenerator; 
	final static int SEC = 1000;
	
	public ACrawler()
	{
		super();
		m_collector = new WgetCollector();
		m_randomGenerator = new Random();
	}
			
	protected boolean ForceDownLoadFile(String filePath,String UrlPath)
	{
		boolean fileExist = FileServices.PathExist(filePath);
		if (fileExist)
		{
			FileServices.DeleteFile(this.GetClassName(), filePath);
			
		}
		return  m_collector.SaveDataFile(filePath, UrlPath);
	}
	
	protected boolean DownloadFile(String filePath,String UrlPath) throws InterruptedException
	{
		boolean fileExist = FileServices.PathExist(filePath);
		if (fileExist && FileServices.NumberDaysFileNotModified(filePath) < 200)
		{
			WriteLineToLog("don't need to download "+filePath +"file allready exist...",ELogLevel.INFORMATION);
			return true;
		}
		if (fileExist)
		{
			
			FileServices.DeleteFile(this.GetClassName(), filePath);
		}
		if (CoreContext.MAX_RUNNERS > 3) 
		{
			try
			{
				int sleepTime = m_randomGenerator.nextInt(3500);
				//WriteLineToLog("going to sleep for: "+sleepTime, ELogLevel.VERBOS);
				Thread.sleep(sleepTime);
			}
			catch (InterruptedException e) 
			{
				WriteLineToLog("Error occur when trying to sleep e="+e.getMessage(), ELogLevel.ERROR);
			}
		}
		
		WgetCollectorExecutors collectorExecutors = new WgetCollectorExecutors(UrlPath, filePath);
		Thread collectorThread = new Thread(collectorExecutors);
		collectorThread.start();
		try 
		{
			collectorThread.join(CoreContext.COLLECTOR_TIME_OUT_SEC * 1000);
			
		} catch (InterruptedException e) {
			WriteLineToLog("problem with stoping thread ...", ELogLevel.ERROR);
		}
		if (collectorThread.isAlive() && !collectorThread.isInterrupted() )
		{
			WriteLineToLog("collector thread is still alive going to interrupt it ", ELogLevel.WARNING);
			collectorThread.interrupt();
			
		}
		return collectorExecutors.GetResult();
	}
		
		//TimeoutCollectorExecutors collectorExecutors = new TimeoutCollectorExecutors(m_collector, filePath, UrlPath);
		/*
		TimerTask collectorTask = new TimerTask(){  
			public void run()
			{  
				WriteLineToLog("abourting collecotr...",ELogLevel.ERROR);
			}	
		}
		*/
		
		
		
		//TODO check this option -- need to execute with timeout .
		
		//Timer collecroeTimer = new Timer(false);  
		//collecroeTimer.schedule( collectorTask, CoreContext.COLLECTOR_TIME_OUT_SEC*SEC );
		
		
		
		
		 //collectorExecutors.CollectWithTimeout(30);
		//collecroeTimer.cancel();
		
	
	
	protected void PrintErrorParsing(Exception e,String cralwingType)
	{
		WriteLineToLog("Error on " +cralwingType +" error : "+ e.toString(),ELogLevel.ERROR);
		//WriteToLog(e.toString(),ELogLevel.ERROR);
		//e.printStackTrace();
	}
	
}
