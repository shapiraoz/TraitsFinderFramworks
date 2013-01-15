package Core;

import Core.Interfaces.ICrawler;

public abstract class ACrawlerFactory extends CommonCBase
{
	protected static ACrawlerFactory m_instance;
	
	
	protected ACrawlerFactory()
	{
		
	}
	
	public static ACrawlerFactory GetInstance()
	{
		return m_instance;
	}
	
	 //need to override
	public ICrawler GenerateCrawler(ECrawlingType type , Object[] prams)
	{
		return null;
	}
	
}
