package Services.Neo4J;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.nioneo.store.NeoStore;

import Core.CoreContext;


public class Neo4JActivation 
{
	
	private static boolean m_isActive;
	private static GraphDatabaseService m_DBservice =null;
	private static Neo4JActivation m_instance;
	private static Map<String,String> m_config ;
	
	
	
	public GraphDatabaseService GetGraphDatabaseService()
	{
		//GetInstance();
		return m_DBservice;
	}
	
		
	public static Neo4JActivation GetInstance()
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
		
		//NeoStore neostore = new ;
		//neostore.nodestore.db.mapped_memory=0;
		
	}
			
	public static boolean Start(String DBpath)
	{
		if (!CoreContext.DB_MEMORY_MODE)
			{m_DBservice =  new GraphDatabaseFactory().newEmbeddedDatabase( DBpath );}
		else
		{
			m_config = new HashMap<String, String>();
			if (CoreContext.DB_MEMORY_MODE)
			{
				
				m_config.put("cache_type","strong");
				m_config.put("neostore.nodestore.db.mapped_memory", "0");
				m_config.put("neostore.relationshipstore.db.mapped_memory", "0");
				m_config.put("neostore.propertystore.db.mapped_memory", "0");
				m_config.put("neostore.propertystore.db.strings.mapped_memory", "0");
				m_config.put("neostore.propertystore.db.arrays.mapped_memory","0");
				
			}
			m_DBservice = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(DBpath).setConfig(m_config).newGraphDatabase();
			
		}
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
