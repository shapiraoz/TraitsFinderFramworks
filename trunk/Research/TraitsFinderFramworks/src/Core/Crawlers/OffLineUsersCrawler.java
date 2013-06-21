package Core.Crawlers;

import java.io.File;

import Core.CoreContext;

import Core.QueueCrawlinTargets;

import Core.Interfaces.ICrawler;
import Elements.EProperty;
import Elements.IElement;
import Elements.StringDataElement;
import Services.FileServices;
import Services.Log.ELogLevel;

public class OffLineUsersCrawler extends ACrawler implements ICrawler{

	
	private String m_userPath ;
	
	public OffLineUsersCrawler()
	{
		Init(CoreContext.OFF_LINE_PATH);
	}
	
	public OffLineUsersCrawler(String userPath)
	{
		Init(userPath);
		
	}
	
	private void Init(String userPath)
	{
		if(!FileServices.PathExist(userPath))
		{
			WriteLineToLog("no users folder to Path ", ELogLevel.ERROR);
		}
		m_userPath = userPath;
	}
	
	@Override
	public IElement Crawl(boolean recursive)
	{
		
		IElement mainElement = null;
		String dir = m_userPath;
						
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();
		
		if (listOfFiles.length >0 )
		{
			String CrawlingMsg = "found directories adding Tragets according to Targets folder....";
			WriteLineToLog(CrawlingMsg, ELogLevel.INFORMATION);
			WriteToConsole(CrawlingMsg);		
			mainElement = new StringDataElement(EProperty.name.toString(),"main");
		}
		for (int i = 0; i < listOfFiles.length; i++) 
		{
			if (listOfFiles[i].isDirectory())
			{
				WriteLineToLog("add Target" +listOfFiles[i].getName(), ELogLevel.INFORMATION);
				//UsersCrawlingTargets.GetInstance().AddTarget(listOfFiles[i].getName());
				QueueCrawlinTargets.GetInstance().AddTarget(listOfFiles[i].getName());
			}
			WriteLineToLog("targes=" + QueueCrawlinTargets.GetInstance().NumbertOfTargets(), ELogLevel.INFORMATION);
		}
		return mainElement ;
	}

	@Override
	public boolean SaveItem() {
		// 

		return false;
	}

}
