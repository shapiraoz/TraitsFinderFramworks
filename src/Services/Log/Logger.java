package Services.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import Services.FileServices;

import Core.CoreContext;

public class Logger
{
	final static String LOG_NAME ="TraitsFinder.log";
		
	public static Logger GetLogger()
	{
		if (m_logger==null)
		{
			if (new File(LOG_NAME).exists())
			{
				File file = new File(LOG_NAME);
				file.delete();
			}
			m_logger = new Logger(LOG_NAME);
		}
		return m_logger;
				
	}
	
	public static void SetPrintLevel(ELogLevel printLevel)
	{
		m_printLogLevel = printLevel;
	}
	
	public static void Write(String module, ELogLevel logLevel,String msg)
	{
		if (logLevel.compareTo(m_printLogLevel)>=0)
		WriteStringToFile(String.format("|%s|[%-21s]-[%s:%s]",GetTimeStr(), module,logLevel.toString(),msg),m_filePath);
	}
	
	public static void WriteLine(String module, String msg,ELogLevel logLevel)
	{
		if (logLevel.compareTo(m_printLogLevel)>=0)
		WriteStringToFile(String.format("|%s|[%-27s]-[%s:%s]\n",GetTimeStr(), module,logLevel.toString(),msg),m_filePath);
	}
	
	private Logger(String filePath)
	{
		m_filePath = filePath;
		m_printLogLevel = CoreContext.LOGGER_LEVEL;
	}
	
	
	static boolean WriteStringToFile(String str,String filePath )
	{
		return FileServices.WriteStringToFile(filePath, str,true);
		/*
		BufferedWriter out = null;
		try
		{
			  fstream = new FileWriter(filePath,true);
			  out = new BufferedWriter(fstream);
			  out.write(str);
			  out.close();
		
		}
		catch (Exception e)
		{
			if (out != null)
			{
				try { out.close();} catch (IOException e1){}
				return false;
			}
		}
		return true;
			*/
	}

	private static String GetTimeStr()
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	
	private static FileWriter fstream =null;
	private static Logger m_logger = null;
	private static String m_filePath;
	private static ELogLevel m_printLogLevel;
}
