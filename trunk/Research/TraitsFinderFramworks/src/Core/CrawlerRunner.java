package Core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;



import Core.Interfaces.ICrawler;
import Elements.IElement;
import Services.FileServices;

public class CrawlerRunner extends Thread
{

	private ICrawler m_crawler ;
	
	
	private ReentrantReadWriteLock m_lockObjFactory;
	private Lock m_writeLockObj;
	private IElement m_headElement;
	
	
	public void SetHeadElement(IElement headElement)
	{
		this.m_headElement =headElement;
	}

	public void setCrawler(ICrawler crawler) 
	{
		this.m_crawler = crawler;
	}

	public void setRecursive(boolean recursive) {
		this.m_recursive = recursive;
	}

	public void Init()
	{
		m_crawler = null;
		m_recursive = false;
		m_element = null;
		m_lockObjFactory = new ReentrantReadWriteLock();
		m_writeLockObj = m_lockObjFactory.writeLock();
	}
	
	private boolean m_recursive;
	
	private IElement m_element = null;
	
	public IElement getElement() 
	{
		IElement returnelm = m_element;
		m_element = null;
		return returnelm;
	}

	public CrawlerRunner(String lock)
	{
		//m_lock = lock;
		
	}
	
	public CrawlerRunner(ICrawler crawler , boolean recursive,String Namelock )
	{
		Init();
		m_crawler = crawler;
		m_recursive = recursive;
		//m_lock = Namelock;
	}
	
	
	public void run() 
	{
		
		 
		
			if (m_crawler == null) return ;
			IElement elm = m_crawler.Crawl(m_recursive);
			if (elm!=null && m_headElement!=null)
			{
				if (CoreContext.SET_GRAPH) 
				{
					elm.Serialize(); //write to graph 
				}
				
				try {
					if (!m_writeLockObj.tryLock(8, TimeUnit.SECONDS))
					{
						return ;
					}
					else
					{
						try
						{
							m_headElement.AddElement(elm);
						}
						finally
						{
							m_writeLockObj.unlock();
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		
		
		
		//interrupt();
		//notify();
	}
	
	

}
