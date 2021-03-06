package Core.Serialization;


import Elements.IElement;
import Services.FileServices;
import Services.Log.ELogLevel;
import Services.Neo4J.Neo4JActivation;
import Services.Neo4J.Neo4JServices;
import Services.Neo4J.Neo4jServicesException;


public class Neo4JSerializer extends ASerializer implements IElementSerializer  {

	
	//private Neo4JActivation m_DBActivation;
	
	private Neo4JServices m_neoServies ;
	private boolean m_isTxWasEnabled = false;
	//private boolean m_enableTx; //TODO:: check if this the solution  
	public Neo4JSerializer(IElement element,String dbDir)
	{
		super(element);
		if (!Neo4JActivation.IsActive())
		{
			WriteLineToLog("Activate Neo4J database...", ELogLevel.INFORMATION);
			if (!FileServices.PathExist(dbDir)) FileServices.CreateFolder(GetClassName(), dbDir);
			if (!Neo4JActivation.IsActive())
			{
				if (!Neo4JActivation.Start(dbDir)) 
				{
					WriteLineToLog("database didn't started...",ELogLevel.ERROR);
					
				}
			}
			m_neoServies = new Neo4JServices(Neo4JActivation.GetInstance().GetGraphDatabaseService());
			if (m_neoServies==null)
			{
				WriteLineToLog("no neo4j service !!!",ELogLevel.ERROR);
			}
			else
			{
				WriteLineToLog("Neo4J is is active", ELogLevel.INFORMATION);
			}
			return;
			
		}
		m_neoServies = new Neo4JServices(Neo4JActivation.GetInstance().GetGraphDatabaseService());
	}
	
	

	@Override
	public boolean Save(boolean close)
	{
		if (m_neoServies==null)
		{
			WriteLineToLog("neo4jService is null", ELogLevel.ERROR);
			return false;
		}
		return  (m_neoServies.CreateNode(m_element,close)!=0);
	}

	@Override
	public boolean Link(IElement elemet)
	{
		if (m_element == null)
		{
			WriteLineToLog("no serializer have no element", ELogLevel.ERROR);
			return false;
		}
		
		if ( m_neoServies == null)
		{
			WriteLineToLog("no neo4j services ", ELogLevel.ERROR);
			return false;
		}
		try 
		{
			return  m_neoServies.AddWeightRelasion(m_element,elemet,m_isTxWasEnabled) ;
		} catch (Neo4jServicesException e) {
			WriteLineToLog("Excpetion msg="+e.getMessage(), ELogLevel.ERROR);
			return false;
		}
	
	}



	@Override
	public IElement Load() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean Close(boolean success) 
	{
		if (m_neoServies!= null)  return m_neoServies.CloseTx(success);
		/*if (Neo4JActivation.IsActive())
		{
			return Neo4JActivation.Stop();
		}
		*/
		return false;
	}

	@Override
	public boolean Save() 
	{
		m_isTxWasEnabled = true;
		return Save(true);
	}



	@Override
	public boolean Open()
	{
		if (m_neoServies!= null)
		{
			return m_neoServies.StartTx();
		}
		return false;
	}



	@Override
	public boolean Close() 
	{
		return Close(true);
	}


/*
	@Override
	public boolean Open()
	{


		return false;
	}
*/
}
