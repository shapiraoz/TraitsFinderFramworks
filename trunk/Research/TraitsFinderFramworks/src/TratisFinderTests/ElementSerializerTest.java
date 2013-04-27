package TratisFinderTests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Core.CoreContext;
import Core.Serialization.ESerializerType;
import Core.Serialization.IElementSerializer;
import Core.Serialization.SerializerFactory;
import Elements.EProperty;
import Elements.EnumElement;
import Elements.IElement;
import Elements.StringDataElement;
import Services.FileServices;

public class ElementSerializerTest extends test{

	String m_testedFile = CoreContext.ROOT_DATA_FOLDER +  "/testSer.tfe";
	String m_loadingFile = CoreContext.ROOT_DATA_FOLDER + "/forLoading.tfe";
	
	private final static String M = "mother";
	private final static String NAME1 = "koko";
	private final static String DES = "this is the discription of elm1 for testing";
	private final static String NAME2 = "blabla";
	
	
	IElement m_elm1 ;
	IElement m_elm2 ;
	IElement m_elmm ;
	IElement m_loadedElem;
	IElementSerializer m_testSeriilizer ; 
	
	public ElementSerializerTest()
	{
		super();
	}
			
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		m_elmm = new StringDataElement();
		m_elmm.SetName(M);
		m_elm1 = new StringDataElement();
		m_elm1.SetName(NAME1);
		m_elm1.AddProperty(EProperty.description.toString(), DES);
		m_elm2 = new StringDataElement();
		m_elm2.SetName(NAME2);
		m_elmm.AddElement(m_elm1);
		m_elmm.AddElement(m_elm2);
		m_elmm.AddSerializer(SerializerFactory.GetInstance().AllocateSerializer(ESerializerType.eElemnetObj,m_elmm, m_testedFile));
		m_testSeriilizer = SerializerFactory.GetInstance().AllocateSerializer(ESerializerType.eElemnetObj,m_loadedElem,m_loadingFile );
		//elmm.AddSerializer(testSeriilizer);
	}

	@Test
	public void testElementObjectSerializer() 
	{
		//fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		if (FileServices.PathExist(m_testedFile) ) 	FileServices.DeleteFile(getClass().getName(),m_testedFile );
		m_elmm.Serialize();
		assertTrue(FileServices.PathExist(m_testedFile));
				
	}

	@Test
	public void testLoad() 
	{
		assertTrue(FileServices.PathExist(m_loadingFile));
		IElement loadelm =  m_testSeriilizer.Load();
		assertTrue(loadelm != null);
		assertTrue(loadelm.GetName().compareTo(M) == 0);
		List<IElement> elms =  loadelm.GetElements();
		for(IElement elm : elms)
		{
			assertTrue(elm.GetName().compareTo(NAME1) == 0 || elm.GetName().compareTo(NAME2) == 0);
		}
				
	}

}
