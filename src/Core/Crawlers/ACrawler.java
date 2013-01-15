package Core.Crawlers;

import java.util.Random;

import Core.CommonCBase;
import Core.CoreContext;
import Services.FileServices;
import Services.ICollector;
import Services.WgetCollector;
import Services.Log.ELogLevel;


public class ACrawler  extends CommonCBase
{
	protected ICollector m_collector;
	protected Random m_randomGenerator ;
	
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
	
	protected boolean DownloadFile(String filePath,String UrlPath)
	{
		
		boolean fileExist = FileServices.PathExist(filePath);
		if (fileExist && FileServices.NumberDaysFileNotModified(filePath) < 80)
		{
			WriteLineToLog("don't need to download file allready exist...",ELogLevel.VERBOS);
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
				int sleepTime = m_randomGenerator.nextInt(2800);
				WriteLineToLog("going to sleep for: "+sleepTime, ELogLevel.VERBOS);
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) 
			{
				WriteLineToLog("Error occur when trying to sleep e="+e.getMessage(), ELogLevel.ERROR);
			}
		}
		return  m_collector.SaveDataFile(filePath, UrlPath);
	}
	
	protected void PrintErrorParsing(Exception e,String cralwingType)
	{
		WriteLineToLog("Error on " +cralwingType +" error : "+ e.toString(),ELogLevel.ERROR);
		//WriteToLog(e.toString(),ELogLevel.ERROR);
		//e.printStackTrace();
	}
	
}
