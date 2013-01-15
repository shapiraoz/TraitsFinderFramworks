package TratisFinderTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Core.CommonCBase;
import Core.CoreContext;
import Core.Serialization.ESerializerType;
import Core.Serialization.IElementSerializer;
import Core.Serialization.SerializerFactory;
import Elements.IElement;
import Elements.EProperty;
import Elements.SubjectElement;
import Services.FileServices;
import Services.Log.ELogLevel;
import Services.Neo4J.Neo4JActivation;
import Services.Neo4J.Neo4JServices;

public class Neo4jSerializerTest extends CommonCBase{

	private final static String PROP_NAME1 = "car";
	private final static String PROP_NAME2 = "pizza";
	
	private final static String PROP_DES1  = "bmw m3";
	private final static String PROP_DES2 = "napoly pizza";
	
	private String m_DBdir = CoreContext.ROOT_DATA_FOLDER+"TestDBRoot/"; 
	IElement m_element ;
	IElement m_element2;
	IElementSerializer m_serializer;
	IElementSerializer m_serializer2;
	
	@Before
	public void setUp() throws Exception
	{
		InitElement();
		m_serializer =  SerializerFactory.GetInstance().GetSerializer(ESerializerType.eNeo4J, m_element, m_DBdir);
		m_serializer2 = SerializerFactory.GetInstance().GetSerializer(ESerializerType.eNeo4J,m_element2, m_DBdir);
	}

	
	void InitElement()
	{
		
		m_element = new SubjectElement(PROP_NAME1);
		m_element.AddProperty(EProperty.description.toString(),PROP_DES1);
		m_element2 = new SubjectElement(PROP_NAME2);
		m_element2.AddProperty(EProperty.description.toString(), PROP_DES2);
	}
	
	@After
	public void tearDown() throws Exception 
	{
		m_serializer.Close();
	}

	@Test
	public void testNeo4JSerializer()
	{
		assertTrue(FileServices.PathExist(m_DBdir));
		assertTrue(m_serializer!=null);
		assertTrue(m_serializer2 !=null);
		
	}

	@Test
	public void testSave()
	{
		assertTrue(Neo4JActivation.IsActive());
		Neo4JServices ns = new Neo4JServices(Neo4JActivation.GetGraphDatabaseService());
		assertTrue(m_serializer.Save());
		long elementId = ns.GetNodeElementId(m_element);
		WriteLineToLog("the element id is : "+elementId , ELogLevel.INFORMATION);
		assertTrue(elementId != CoreContext.NOT_EXIST_IN_DB);
		assertTrue(ns.GetNodeProperty(m_element, EProperty.name.toString()).compareTo(PROP_NAME1 )==0);
		assertTrue(ns.GetNodeProperty(m_element, EProperty.description.toString()).compareTo(PROP_DES1)==0 );	
		assertTrue(m_serializer2.Save());
		assertTrue(ns.AddWeightRelasion(m_element, m_element2))	;	
	}

	@Test
	public void testSaveIElement() {
		fail("Not yet implemented");
	}

	@Test
	public void testLoad() {
		fail("Not yet implemented");
	}

}
