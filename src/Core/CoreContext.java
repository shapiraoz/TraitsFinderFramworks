package Core;

import Services.FileServices;
import Services.Dom.DomDocument;
import Services.Log.ELogLevel;

public class CoreContext 
{
	
	protected static ACrawlerProcessor m_processor;
	
	public final static String CORE_VERSION = "1.1.5.4_X";
	
	public final static String ROOT_DATA_FOLDER = "data/";
	
	public final static String EMPTY="";
	
	////////////////////////////////////////////////////////////////////
	//DB logic:
	public final static long NOT_EXIST_IN_DB = -1;
	public final static int NOT_EXIST = -1;
	public static String GRAPH_DB_DIR = ROOT_DATA_FOLDER + "DB_DIR/";
	public final static String NEO_WEIGHT = "weight";	
	public static boolean SET_GRAPH = false;
	public static String VISIT_NAME = "visited.txt";
	
	public static boolean DB_MEMORY_MODE = false;
	//////////////////////////////////////////////////////////////////////
	//running configuration 
	public static int MAX_RUNNERS = 3;
	public static long MAX_CRAWLING_USER = 30;
	public static boolean OFF_LINE_MODE =false;
	//default log level
	public static ELogLevel LOGGER_LEVEL = ELogLevel.WARNING;
	
	public static int MAX_NUMBER_IN_Q = 100000;  //Integer.MAX_VALUE -2;	
	
	public static String OFF_LINE_PATH = "OffLine" ;
	public final static int COLLECTOR_TIME_OUT_SEC = 30;
	
	public ACrawlerProcessor GetProcssor() {return null;}
	///////////////////////////////////////////////////////////////////////
	public static boolean COLLECT_STATISIC = true;
	public static boolean FAST_MODE = false;
	
	
	
	//TODO complete this function 
	public static boolean LoadContext(String filePath)
	{
		boolean res = false;
		if (!FileServices.PathExist(filePath)) return false;
		
		try
		{
			DomDocument dm = new DomDocument(filePath);
			
		}
		catch (Exception ex)
		{
			
		}
		return res;
	}
	
	
	
}
