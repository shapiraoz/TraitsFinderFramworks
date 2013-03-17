package TratisFinderTests;



import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import Core.CommonCBase;
import Core.CoreContext;
import Services.FileServices;
import Services.Log.ELogLevel;
import Services.Log.Logger;

public class test extends CommonCBase {

	
	
	@Before
	public void setUp() throws Exception
	{
		Logger.GetLogger();
		CoreContext.LOGGER_LEVEL = ELogLevel.INFORMATION;
		if (!FileServices.PathExist(CoreContext.ROOT_DATA_FOLDER))
		{
			FileServices.CreateFolder(this.getClass().getName(), CoreContext.ROOT_DATA_FOLDER);
		}
		//if (!FileServices.PathExist(CoreContext.USERS_FOLDER_POOL_PATH)) FileServices.CreateFolder(GetClassName(),CoreContext.USERS_FOLDER_POOL_PATH);
		
	}

	@After
	public void tearDown() throws Exception {
	}
 
	@Test
	public void T1test() 
	{
		String s ="/marenbaarlid/been-there/";
		String s1[] = s.split("/");
		System.out.println(s1[2]);
		assertTrue(s1[2].compareTo("been-there")==0);
		
	}
	

}
