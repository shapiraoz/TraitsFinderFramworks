package Services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import Services.Log.ELogLevel;
import Services.Log.Logger;

import Core.CoreContext;

public class FileServices 
{
	
	public static boolean DeleteFolder(String module,String path)
	{
		File dir = new File(path);
		return deleteDir(dir);
	}
		
	private static boolean deleteDir(File dir) {
	if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	}
	   // The directory is now empty so delete it
	    return dir.delete();
	}	
	
	
	public static boolean CreateTextFile(String module,String filePath)
	{
		File file = new File(filePath);
		try 
		{
			return  file.createNewFile();
		} 
		catch (IOException e) 
		{
			WriteLineToLog(module,"failed to create file ", ELogLevel.ERROR);
		}
		return false;
		
	}
	
	public static boolean DeleteFile(String module,String path)
	{
		File fileElm = new File(path);
		if (fileElm.exists())
		{
			fileElm.delete();
			WriteLineToLog(module, "the path " + path+" have been deleted",ELogLevel.INFORMATION);
			return true;
		}
		WriteLineToLog(module, "failed to delete " +path +" path not exist ",ELogLevel.ERROR);
		return false;
	}
	
	
	
	public static  boolean CreateFolder(String module, String Path)
	{
		try
		{
			   // Create one directory
			  boolean success = (
			  new File(Path)).mkdir();
			  if (success) 
			  {
				  WriteLineToLog(module, "directory has created at "+Path,ELogLevel.INFORMATION);
				  return true;
			  }
			  else
			  {
				  WriteLineToLog(module,"failed to create directory :  " + Path,ELogLevel.ERROR); 
				return false;
			  }
			
		}
		catch (Exception e)
		{
			//Catch exception if any
			WriteLineToLog(module ,"error when creating directory Error: " +e.getMessage(),ELogLevel.ERROR); 
			return false;
		}
		
	  
	}
	
	public static boolean WriteStringToFile(String filePath,String str ,boolean append)
	{
		BufferedWriter out = null;
		try
		{
			  FileWriter fstream = new FileWriter(filePath,append);
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
		
	}
	
	public static boolean PathExist(String path)
	{
		return new File(path).exists();
	}
	
	private static void WriteLineToLog(String module, String msg,ELogLevel logLevel)
	{
		Logger.WriteLine(module,msg,logLevel);
	}

	public static int NumberDaysFileNotModified(String path)
	{
		File file = new File(path);
		if (!file.exists()) return CoreContext.NOT_EXIST;
		int lastModified = (int) new Date(file.lastModified()).getDay();
		int today = new Date().getDay();
		return today-lastModified;
	}
	
	
}
