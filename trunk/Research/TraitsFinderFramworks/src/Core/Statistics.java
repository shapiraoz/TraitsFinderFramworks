package Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import Elements.EProperty;
import Elements.IElement;
import Services.Log.ELogLevel;

public class Statistics extends CommonCBase
{
	
		
	protected Map<String,List<String>> m_subTargetTargetDB;
	protected Map<String,List<String>> m_TargetToSubTargetDB;
	protected Map<EProperty,List<IElement>> m_commonProperties;
	protected ReentrantReadWriteLock m_lockObjFactory;
	protected Lock m_writeLock;
	
	private final int TIME_TO_WAIT_SEC = 2;
	
	public  Statistics m_instance;
	
	public Statistics()
	{
		//						 //Static item , list of frequent to this item (targets)
		m_subTargetTargetDB = new HashMap<String, List<String>>();
		m_TargetToSubTargetDB = new HashMap<String, List<String>>();
		m_commonProperties = new HashMap<EProperty,List< IElement>>();
		m_lockObjFactory = new ReentrantReadWriteLock();
		m_writeLock = m_lockObjFactory.writeLock();
	}
	
	public Map<String, List<String>> getSubTargetTargetDB() {
		return m_subTargetTargetDB;
	}

	public Map<String, List<String>> getTargetToSubTargetDB() {
		return m_TargetToSubTargetDB;
	}

	
	
	public boolean AddHit(String target,String subTarget)
	{
		boolean ret =true;
		try
		{
			ret &= AddHitToDB(m_TargetToSubTargetDB,target , subTarget);	
			ret &= AddHitToDB(m_subTargetTargetDB, subTarget, target);
			if(ret)
			{
				WriteLineToLog("add to statistics ["+target + "," +subTarget+ "]" , ELogLevel.INFORMATION);
			}
		}
		catch (Exception ex)
		{
			ret &= false;
		}
		return ret;
	}
	
	public boolean AddHit(IElement target ,IElement subTarget)
	{
		return	AddHit(target.GetName(),subTarget.GetName());
	}
											//user				subject 
	public boolean AddHit(EProperty propType,IElement target ,IElement uesdBytarget) // TODO oz:: finish this 
	{
		boolean res = false;
		if (m_commonProperties.containsKey(propType) )
		{
			m_commonProperties.get(propType).add(target);
		}
		else
		{
			List<IElement> targets = new ArrayList<IElement>();
			targets.add(target);
			m_commonProperties.put(propType,targets );
		}
		//m_staticDB.put(propType.toString(), )
				
		return res;
	}
	
	public List<String> GetTargetsHits(String subTarget)
	{
		if (!m_subTargetTargetDB.containsKey(subTarget)) return null;
		return m_subTargetTargetDB.get(subTarget);
	}
	
	public List<String> GetSubTargetsHits(String target)
	{
		if (!m_TargetToSubTargetDB.containsKey(target)) return null;
		return m_TargetToSubTargetDB.get(target);
	}
	
	private boolean AddKeyTarget(Map<String,List<String>> mapDB, String key)
	{
		if (!mapDB.containsKey(key)) 
		{
			mapDB.put(key, new LinkedList<String>());
		}
		return mapDB.containsKey(key);
	}
	
	private boolean AddHitToDB(Map<String,List<String>> mapDb,String Key,String Val)
	{
	
		if (mapDb.containsKey(Key))// improve this - this is a bad code....
		{
			
			try {
				if ( m_writeLock.tryLock(TIME_TO_WAIT_SEC,TimeUnit.SECONDS) )
				{
					//WriteLineToLog("take the lock....", ELogLevel.VERBOS);
					mapDb.get(Key).add(Val);
					m_writeLock.unlock();
					//WriteLineToLog("unlock key= "+Key +" value="+Val +" have been added!!", ELogLevel.VERBOS);
					return true;
				}
			} catch (InterruptedException e)
			{
				WriteLineToLog("faild to add hit key =" + Key + "val = " +Val , ELogLevel.ERROR);
				return false;
			}
		}
		else
		{
			try {
				if (m_writeLock.tryLock(TIME_TO_WAIT_SEC,TimeUnit.SECONDS))
				{
					AddKeyTarget(mapDb, Key);
					mapDb.get(Key).add(Val);
					m_writeLock.unlock();
					//WriteLineToLog("unlock key= "+Key +" value="+Val +" have been added!!", ELogLevel.VERBOS);
					return true;
				}
			}
			catch (InterruptedException e) 
			{
				WriteLineToLog("faild to add hit key =" + Key + "val = " +Val , ELogLevel.ERROR);
				return false;
			}
		}
		return false;
	}
	
	
	
	
}
