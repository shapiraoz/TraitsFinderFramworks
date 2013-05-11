package Core;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import Services.Log.ELogLevel;


import Core.Interfaces.ICrawlingTargets;

public class QueueCrawlinTargets extends CommonCBase implements ICrawlingTargets
{

	private Queue<String> m_targets ;
	private ReentrantReadWriteLock m_lockObjFactory;
	private Lock m_writeLockObj;
	private int MAX_LOCK_TRY=2;
	
	
	private static QueueCrawlinTargets m_instance ;
	
	
	private QueueCrawlinTargets()
	{
		m_targets = new LinkedList<String>();
		m_lockObjFactory = new ReentrantReadWriteLock();
		m_writeLockObj = m_lockObjFactory.writeLock();
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
			 exist= m_targets.contains(starget);
			// WriteLineToLog("exist="+exist, ELogLevel.VERBOS);
			 return exist;
		}
		catch (Exception e) 
		{
			WriteLineToLog("exception happen msg="+e.getMessage(), ELogLevel.ERROR);
		}
		return exist;
	}
	
	@Override
	public long NumbertOfTargets()
	{
		long size=CoreContext.NOT_EXIST;
		try
		{
			size=  m_targets.size();
			//WriteLineToLog(" size="+size, ELogLevel.VERBOS);
			return size;
				
		}
		catch (Exception e) 
		{
			WriteLineToLog("exception happen msg="+e.getMessage(), ELogLevel.ERROR);
		}
		return size;
		
	}
	
	@Override
	public String GetNextTarget() {
		
		String nextTarget ="";
		
		try
		{
			nextTarget= m_targets.poll();
			//WriteLineToLog("nextTarget="+nextTarget, ELogLevel.VERBOS);
			return nextTarget;
		}
		catch (Exception e) 
		{
			WriteLineToLog("exception happen msg="+e.getMessage(), ELogLevel.ERROR);
		}
		return nextTarget;
				
	}
	
	

	@Override
	public boolean AddTarget(String sTarget)
	{
		if (sTarget!=null)
		{
			
					try {
						if ( m_writeLockObj.tryLock(MAX_LOCK_TRY,TimeUnit.SECONDS))
						{
							// WriteLineToLog("AddTarget::success to lock target="+ sTarget, ELogLevel.VERBOS);
							 if (m_targets.size()<= CoreContext.MAX_NUMBER_IN_Q && !IsExist(sTarget)) m_targets.add(sTarget);
							 m_writeLockObj.unlock();
							 //WriteLineToLog("unlock", ELogLevel.VERBOS);
							 return true;
						}
					} catch (InterruptedException e) {
						WriteLineToLog("exception happen msg=" + e.getMessage(), ELogLevel.ERROR);
					}
					
			}
		return false;
	}
	
}
