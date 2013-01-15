package Services;

import java.net.URL;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;

import Core.CommonCBase;
import Services.Log.ELogLevel;

public class WgetCollector extends CommonCBase implements ICollector {

	
	public WgetCollector()
	{
		
	}
	
	
	@Override
	public boolean SaveDataFile(String PathToSave,String address) {
	
		WriteLineToLog(" save url "+ address+" to clean file, file path : "+ PathToSave +" ....",ELogLevel.INFORMATION);
		//Logger.GetLogger().WriteLine(m_Name, " save url "+ address+" to clean file, file path : "+ PathToSave +" ....");
		CleanerProperties props = new CleanerProperties();
		 
				// set some properties to non-default values
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(true);
		props.setOmitComments(true);
		 
		// do parsing
		TagNode tagNode;
		try
		{
			tagNode = new HtmlCleaner(props).clean( new URL(address));
			// serialize to xml file
			
			new PrettyXmlSerializer(props).writeToFile(
				    tagNode, PathToSave, "utf-8"
				);
			WriteLineToLog("done saving file!! save on : "+PathToSave ,ELogLevel.INFORMATION);
			return FileServices.PathExist(PathToSave);
		} 
		catch (Exception e) {
			WriteLineToLog("Error "+e.toString(),ELogLevel.ERROR);
			//WriteLineToLog(e.getCause().toString(),ELogLevel.ERROR);
			//Logger.GetLogger().WriteLine(m_Name,"Error "+e.toString());
			//Logger.GetLogger().WriteLine(m_Name, e.getCause().toString());
			return false;
		}
		 
						
	}
	

	

	
}
