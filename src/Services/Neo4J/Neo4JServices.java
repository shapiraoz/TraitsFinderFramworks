package Services.Neo4J;




import java.util.Iterator;
import java.util.Map;


import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.TransactionFailureException;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.kernel.impl.core.TxEventSyncHookFactory;

import scala.util.parsing.combinator.Parsers.Success;

import Core.CommonCBase;
import Core.CoreContext;
import Elements.IElement;
import Elements.EProperty;
import Services.Log.ELogLevel;

@SuppressWarnings("finally")
public class Neo4JServices extends CommonCBase 
{
	
	final static protected int RETRIES = 3;
	protected GraphDatabaseService m_services;
	protected Index<Node> m_indexNode;
	protected Transaction m_tx;
	//private Index<Relationship> m_indexRelation; 
			
	public Neo4JServices(GraphDatabaseService service)
	{
		
			m_services = service;
			m_indexNode =  m_services.index().forNodes(EProperty.name.toString());
			m_tx = null;
			
			//m_indexRelation = m_services.index().forRelationships(RelType.Users.toString());
	}
	
	public long CreateNode(IElement element)
	{
		return CreateNode(element,true);
	}
			
	public long CreateNode(IElement element ,boolean enableTx)
	{
		Node tempNode =null;
		if (m_services ==null)
		{
			WriteLineToLog("no Neo4j servies",ELogLevel.ERROR);
			return 0;
		}
		if (enableTx) 
		{
			StartTx();
		}
		try
        {
			String name = element.GetProperty(EProperty.name.toString()).toString();
			IndexHits<Node> hits = m_indexNode.get(EProperty.name.toString(), name);
			if (hits.size() == 0)
			{
				for (int i=0; i < RETRIES ; ++i)
				{
					tempNode =  m_services.createNode();
					if (tempNode != null) break;
				}
				//tempNode.setProperty(EProperty.name.toString(),name );
				m_indexNode.add(tempNode, EProperty.name.toString(),name );
				WriteLineToLog("node created id: "+ tempNode.getId(), ELogLevel.INFORMATION);
			}
			else
			{
				tempNode =  hits.getSingle();
				WriteLineToLog("found node id:" +tempNode.getId(), ELogLevel.INFORMATION);
			}
					
			
			Iterator itr =  element.GetProperties().entrySet().iterator();
			while (itr.hasNext())
			{
				Map.Entry pair = (Map.Entry) itr.next();
				tempNode.setProperty(pair.getKey().toString(), pair.getValue().toString());
				WriteLineToLog("add prop to node: propkey="+ pair.getKey().toString()+" propvalue= " +pair.getValue().toString(), ELogLevel.INFORMATION);
			}
			if (enableTx) 
			{
				if (! CloseTx(true))
				{
					WriteLineToLog("Error, failed to close transction", ELogLevel.ERROR);
				}
			}
			
        }
		catch(Exception ex )
		{
			WriteLineToLog("transaction failed:" +ex.getMessage(), ELogLevel.ERROR);
			if (enableTx && m_tx!=null) CloseTx(false);
			return 0;
		}
		return tempNode.getId();
		
	}
	
	public boolean StartTx()
	{
		m_tx = m_services.beginTx();
		return true;
	}
	
	public boolean CloseTx(boolean success)
	{
		try
		{
			if (m_tx != null)
			{
				if (success) m_tx.success();
				else m_tx.failure();
					
				m_tx.finish();
				return true;
			}
		}
		catch(TransactionFailureException txEx)
		{
			WriteLineToLog("excpetion transaction close will try to close it", ELogLevel.ERROR);
			if (m_tx != null) m_tx.finish();
		}
		return false;
	}
	
	public long GetNodeElementId(IElement element)
	{
		Node returnNode = m_indexNode.get(EProperty.name.toString(), element.GetName()).getSingle();
		if (returnNode != null) return returnNode.getId();
		return CoreContext.NOT_EXIST_IN_DB;
	}
	
	Relationship FindRelation(Node src ,Node traget)
	{
		boolean found =false;
		for(Relationship rel : src.getRelationships(RelType.Weight,Direction.BOTH))
		{
			found = rel.getEndNode().equals(traget);
			if (found)
			{
				WriteLineToLog("found relation: id="+ rel.getId() , ELogLevel.INFORMATION);
				return rel;
			}
		}
		return null;
	}
	
	//need to be tested - can be the fix that we need !!!!
	Relationship FindRelationQuery(Node src ,Node traget)
	{
		long srcId = src.getId();
		long targetId = src.getId();		
		ExecutionResult result= ExecutCyperQuery("START left=node("+ srcId +"), right=node(" +targetId +") RELATE left-[r:KNOWS]->right RETURN r");
		Iterator<Relationship> rs =  (Iterator<Relationship>) result.columnAs("r");
		if (result.size() > 1) 
		{
			WriteLineToLog("why there is more them one reletion ???", ELogLevel.ERROR);
		}
		
		for (Relationship r :IteratorUtil.asIterable((Iterator<Relationship>) rs))
		{
			return r;
		}
		return null;
	}
	
	public ExecutionResult ExecutCyperQuery(String query)
	{
		ExecutionEngine engine = new ExecutionEngine( Neo4JActivation.GetInstance().GetGraphDatabaseService() );
		return  engine.execute( query);
	}
	
	
	public Relationship AddRelasion(IElement elm1 ,IElement elm2, RelationshipType relType) throws Neo4jServicesException
	{
		return  AddRelasion(elm1,elm2,relType,true);
	}
	
	public Relationship AddRelasion(IElement elm1 ,IElement elm2, RelationshipType relType,boolean enableTx) throws Neo4jServicesException
	{
		Relationship relReturn =null;
		try
		{
			Node elm1Node = m_indexNode.get(EProperty.name.toString(), elm1.GetName()).getSingle();
			Node elm2Node = m_indexNode.get(EProperty.name.toString(), elm2.GetName()).getSingle();
			if (elm1Node.getId() == elm2Node.getId() ) 
			{
				WriteLineToLog("elm1 and elm2 are the same don't need to add relation...", ELogLevel.ERROR);
				return null;
			}
			if (elm1Node == null || elm2Node == null )
			{
				WriteLineToLog("one on the node (elm1Node or elm2Node is null...", ELogLevel.ERROR);
				return null;
			}
					
			do
			{
				relReturn = FindRelation(elm1Node, elm2Node);
				if (relReturn != null) break;
				relReturn = FindRelation(elm2Node, elm1Node);
			}
			while(false);
			if (relReturn == null)
			{
				if (enableTx) StartTx();
				relReturn =  elm1Node.createRelationshipTo(elm2Node,relType);
				WriteLineToLog("new relation between : " +elm1Node.getProperty(EProperty.name.toString())+ " to " +elm2Node.getProperty(EProperty.name.toString())+" created reltype=" +relType.toString(),ELogLevel.INFORMATION );
				
				if (enableTx) CloseTx(true);
			}
			
		}
		catch(Exception ex )
		{
			WriteLineToLog("transaction failed... failed to create relation between "+elm1.GetName() + " to " + elm2.GetName() + "exception = " +ex.getMessage(), ELogLevel.ERROR);
			if (m_tx!=null && enableTx) CloseTx(false);
			else throw new Neo4jServicesException("fail to add relation !!1");
				
		}
		
		return relReturn;

	}
		
	public boolean AddWeightRelasion(IElement elm1 ,IElement elm2 ) throws Neo4jServicesException
	{
		return AddWeightRelasion(elm1,elm2,true);
	}
	
	public boolean AddWeightRelasion(IElement elm1 ,IElement elm2,boolean enableTx) throws Neo4jServicesException
	{
		Relationship rel =  AddRelasion(elm1, elm2, RelType.Weight);
		if (rel == null) 
		{
				WriteLineToLog("failed to add relation ... will not add weight !!!", ELogLevel.ERROR);
				return false;
		}
		boolean status = false;
		if(enableTx) StartTx();	
			
		try
		{
			if (!rel.hasProperty(CoreContext.NEO_WEIGHT))
			{
				WriteLineToLog("set weight to 1",ELogLevel.INFORMATION);
				rel.setProperty(CoreContext.NEO_WEIGHT, 1);
			}
			else
			{
				Integer count = (Integer)rel.getProperty(CoreContext.NEO_WEIGHT);
				WriteLineToLog("the weight is " + count +"increase weight by 1 ",ELogLevel.INFORMATION);
				count++;
				rel.setProperty(CoreContext.NEO_WEIGHT, count);
				WriteLineToLog("weight increased to "+count ,ELogLevel.INFORMATION);
			}
			status = true;
			if (enableTx) CloseTx(true);
				
			return status;
		}
		catch(Exception e)
		{
			WriteToLog("error occur msg=" +e.getMessage(), ELogLevel.ERROR);
			if (m_tx!=null && enableTx) CloseTx(false);
			else throw new  Neo4jServicesException("failed at add wight function ");
				
			return false;
		}
	}
	
	public String GetNodeProperty(long NodeId , String Property)
	{
		return m_services.getNodeById(NodeId).getProperty(Property).toString();
	}
	
	public String GetNodeProperty(IElement element , String Property)
	{
		long nodeId = GetNodeElementId(element);
		if (nodeId != CoreContext.NOT_EXIST_IN_DB) return GetNodeProperty(nodeId, Property);
		return "";
	}
	
		
}
