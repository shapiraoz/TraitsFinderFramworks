package Core;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import Services.Log.ELogLevel;


import Core.Interfaces.ICrawlingTargets;

public class QueueCrawlinTargets extends CommonCBase implements ICrawlingTargets
{

	private Queue<String> m_targets ;
	private ReentrantReadWriteLock m_lockObjFactory;
	private Lock m_writeLockObj;
	private Lock m_readLockObj;
	private int MAX_LOCK_TRY=3;
	private int LOCK_TIME = 250;
	
	private static QueueCrawlinTargets m_instance ;
	
	
	private QueueCrawlinTargets()
	{
		m_targets = new LinkedList<String>();
		m_lockObjFactory = new ReentrantReadWriteLock();
		m_writeLockObj = m_lockObjFactory.writeLock();
		m_readLockObj = m_lockObjFactory.readLock();
	}
	
	public static QueueCrawlinTargets GetInstance()
	{
		if (m_instance==null)
		{
			m_instance = new QueueCrawlinTargets();
		}
		return m_instance;
		
	}
	
	public boolean IsExist(String starget)
	{
		boolean exist=false;
		try
		{
			for (int i= 0;i<MAX_LOCK_TRY ;i++)
			{
			
				if ( m_readLockObj.tryLock())
				{
					 WriteLineToLog("success to lock get the size", ELogLevel.VERBOS);
					 exist= m_targets.contains(starget);
					 m_readLockObj.unlock();
					 WriteLineToLog("unlock", ELogLevel.VERBOS);
					 break;
				}
				Thread.sleep(LOCK_TIME);
			}
			
					
		}
		catch (Exception e) 
		{
			WriteLineToLog("exception happen msg="+e.getMessage(), ELogLevel.ERROR);
			m_readLockObj.unlock();
		}
		return exist;
	}
	
	@Override
	public long NumbertOfTargets()
	{
		long size=CoreContext.NOT_EXIST;
		try
		{
			for (int i= 0;i<MAX_LOCK_TRY ;i++)
			{
			
				if ( m_readLockObj.tryLock())
				{
					 WriteLineToLog("success to lock get the size", ELogLevel.VERBOS);
					 size = m_targets.size();
					 m_readLockObj.unlock();
					 WriteLineToLog("unlock", ELogLevel.VERBOS);
					 break;
				}
				Thread.sleep(LOCK_TIME);
			}
			
					
		}
		catch (Exception e) 
		{
			WriteLineToLog("exception happen msg="+e.getMessage(), ELogLevel.ERROR);
			m_readLockObj.unlock();
		}
		return size;
		
	}
	
	@Override
	public String GetNextTarget() {
		
		String nextTarget ="";
		
		try
		{
			for (int i= 0;i<MAX_LOCK_TRY ;i++)
			{
			
				if ( m_writeLockObj.tryLock())
				{
					 WriteLineToLog("success to lock get next target", ELogLevel.VERBOS);
					 nextTarget= m_targets.poll();
					 m_writeLockObj.unlock();
					 WriteLineToLog("unlock", ELogLevel.VERBOS);
					 break;
				}
				 WriteLineToLog("sleep for half sec...",ELogLevel.VERBOS);
				Thread.sleep(LOCK_TIME);
			}	
					
		}
		catch (Exception e) 
		{
			WriteLineToLog("exception happen msg="+e.getMessage(), ELogLevel.ERROR);
			m_readLockObj.unlock();
		}
		return nextTarget;
				
	}
	
	

	@Override
	public boolean AddTarget(String sTarget)
	{
		if (sTarget!=null)
		{
			try
			{
				for (int i= 0;i<MAX_LOCK_TRY ;i++)
				{
				
					if ( m_writeLockObj.tryLock())
					{
						 WriteLineToLog("success to lock get next target", ELogLevel.VERBOS);
						 if (m_targets.size()<= CoreContext.MAX_NUMBER_IN_Q && !IsExist(sTarget)) m_targets.add(sTarget);
						 m_writeLockObj.unlock();
						 WriteLineToLog("unlock", ELogLevel.VERBOS);
						 return true;
					}
					WriteLineToLog("sleep for half sec...",ELogLevel.VERBOS);
					Thread.sleep(LOCK_TIME);
				}	
						
			}
			catch (Exception e) 
			{
				WriteLineToLog("exception happen msg="+e.getMessage(), ELogLevel.ERROR);
				m_readLockObj.unlock();
			}
		}
		return false;
	}
	
}
