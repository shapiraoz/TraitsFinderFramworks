package Services;


import Services.Log.ELogLevel;

import Core.CommonCBase;

public class WgetCollectorExecutors extends CommonCBase implements Runnable
{

	protected String m_address;
	protected String m_storedPath;
	protected boolean m_result;
	
	public WgetCollectorExecutors(String url, String path)
	{
		m_address = url;
		m_storedPath = path;
		m_result = false;
	}
	
	public boolean GetResult() {return m_result;}
	
	@Override
	public void run() 
	{
		if (m_address.isEmpty() || m_storedPath.isEmpty())
		{
			WriteLineToLog("can't activate collector...no data is empty", ELogLevel.ERROR);
			return;
		}
		WgetCollector collector = new WgetCollector();
		
		m_result = collector.SaveDataFile(m_storedPath, m_address);
	}
	
	
	
}
