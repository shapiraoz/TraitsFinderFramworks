package Services.Neo4J;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class Neo4JActivation 
{
	
	private static boolean m_isActive;
	private static GraphDatabaseService m_DBservice =null;
	private static Neo4JActivation m_instance;
	
	public static GraphDatabaseService GetGraphDatabaseService()
	{
		//GetInstance();
		return m_DBservice;
	}
	
	private static Neo4JActivation GetInstance()
	{
		if (m_instance == null)
		{
			m_instance = new Neo4JActivation();
			
		}
		return m_instance;
	
	}
	
	public static boolean IsActive()
	{
		return (m_isActive && m_DBservice != null);
	}
	
	private Neo4JActivation()
	{
	   // new	GraphDatabaseFactory().newEmbeddedDatabaseBuilder("");
	   
	}
			
	public static boolean Start(String DBpath)
	{
		m_DBservice =  new GraphDatabaseFactory().newEmbeddedDatabase( DBpath );
		RegisterShutdownHook(m_DBservice);
		m_isActive = true;
		return true;
	}
	
	private static void RegisterShutdownHook(final GraphDatabaseService graphDb)
	{
		 Runtime.getRuntime().addShutdownHook( new Thread()
	        {
	            @Override
	            public void run()
	            {
	                graphDb.shutdown();
	            }
	        } );
		
	}

	public static boolean Stop()
	{
		if (m_DBservice != null )
		{
			m_DBservice.shutdown();
			m_isActive = false;
			return true;
		}
		return false;
	}
	
		
	

}
