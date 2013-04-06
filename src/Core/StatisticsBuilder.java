package Core;

import java.util.HashMap;
import java.util.Map;

import Elements.IElement;

public class StatisticsBuilder 
{
	public interface EStatsType{}
	
	protected static StatisticsBuilder m_instance;
	
	public static StatisticsBuilder GetInstance()
	{
		if (m_instance == null) 
		{
			m_instance = new StatisticsBuilder();
		}
		return m_instance;
	}
	
	
	Map<EStatsType, Statistics> m_statistics;
	
	public StatisticsBuilder()
	{
		m_statistics = new HashMap<StatisticsBuilder.EStatsType, Statistics>();
	}
	
	public boolean AddHit(EStatsType type , String target,String subTarget)
	{
		if (!m_statistics.containsKey(type))
		{
			m_statistics.put(type, new Statistics());
		}
		return  m_statistics.get(type).AddHit(target, subTarget);
	}
	
	public boolean AddHit(EStatsType type ,IElement target ,IElement subTarget)
	{
		if (!m_statistics.containsKey(type))
		{
			m_statistics.put(type, new Statistics());
		}
		return m_statistics.get(type).AddHit(target, subTarget);
	}
		
	public Statistics GetStatistics(EStatsType type)
	{
		if  ( m_statistics.containsKey(type) ) return m_statistics.get(type);
		return null;
		
	}
	
}
