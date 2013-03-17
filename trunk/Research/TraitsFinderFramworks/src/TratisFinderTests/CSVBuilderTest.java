package TratisFinderTests;


import org.junit.Before;
import org.junit.Test;

import Services.CSVBuilder;
import Services.FileServices;

public class CSVBuilderTest extends test {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		m_builder = new CSVBuilder(m_testedFile);
	}

	@Test
	public void TestWriteHeader() {
		
		String[] head ={"first","last","age"};
		m_builder.WriteHeader(head);
		assert(FileServices.PathExist(m_testedFile));
		assert(m_builder.IsFileCreateWithHeader());		
	}
	
	@Test
	public void TestWriteLine()
	{
		
		TestWriteHeader();
		String[] line= new String[m_builder.GetColumnNumber()] ;
		for (int i =0 ; i< m_builder.GetColumnNumber(); i++)
		{
			line[i] = "col" + i;
		}
		m_builder.WriteLine(line);
		//need to improve test 
	}
	
	String m_testedFile ="test.csv";
	CSVBuilder m_builder ;
	
	
	

}
