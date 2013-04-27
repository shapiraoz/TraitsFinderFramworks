package Core;

import java.io.Serializable;

import Services.Log.ELogLevel;
import Services.Log.Logger;

public abstract class CommonCBase implements Serializable
{
	protected String GetClassName()
	{
		m_Name=this.getClass().getSimpleName();
		return  "" +Thread.currentThread().getId() +":" +m_Name;
	}
	
	protected  void WriteLineToLog(String msg,ELogLevel logLevel)
	{
		Logger.GetLogger().WriteLine(GetClassName(),  msg,logLevel);
	}

	protected void WriteToLog(String msg,ELogLevel logLevel)
	{
		Logger.GetLogger().Write(GetClassName(), logLevel, msg);
	}
	
	protected void WriteToConsole(String msg)
	{
		System.out.println(msg);
	}
	
	public CommonCBase(){}
	
	protected static String m_Name = ""; 
}
