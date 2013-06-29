package Services;

import Services.Log.ELogLevel;
import Core.CommonCBase;

public class CSVBuilder extends CommonCBase 
{
	public CSVBuilder(String CSVfilePath)
	{
		m_filePath = CSVfilePath;
		if (FileServices.PathExist(m_filePath))
		{
			FileServices.DeleteFile(GetClassName(), m_filePath);
		}
			
		m_numColmn = 0;
	}
	
	public boolean WriteHeader(String[] header )
	{
		
		if (!FileServices.PathExist(m_filePath))
		{
			FileServices.CreateTextFile(GetClassName(), m_filePath);
		}
		WriteLineToLog("created the " +m_filePath +" file ", ELogLevel.INFORMATION);		
		if (!WriteInnerLine(header)) return false;
		m_numColmn = header.length;
		
		return true;
	}
		
	public boolean WriteLine(String[] line)
	{
		
		if (!FileServices.PathExist(m_filePath))
		{
			WriteLineToLog("Error csv file was not created can't write to file", ELogLevel.ERROR);
			return false;
		}
		
		
		/*if (line.length < m_numColmn)
		{
			WriteLineToLog("number of lines are less them header ...", ELogLevel.WARNING);
			for (int i = line.length -1 ; i <line.length ; i ++)
			{
				line[i] = "";
			}
		}
		*/
		return  WriteInnerLine(line);
		
	}
	
	public boolean IsFileCreateWithHeader()
	{
		return  (m_numColmn > 0);
	}
	
	public int GetColumnNumber()
	{
		return m_numColmn;
	}
	
	
	private boolean WriteInnerLine(String[] line)
	{
		
		String linestr = "";
		for (String col : line)
		{
			col = col.replace(",","");
			linestr += col +",";
		}
		linestr += "\n";
		
		WriteLineToLog("write line to " + m_filePath+ " file =\n" +linestr, ELogLevel.INFORMATION);
		if  (!FileServices.WriteStringToFile(m_filePath, linestr, true))
		{
			WriteLineToLog("failed to write header file!!!", ELogLevel.ERROR);
			return false;
		}
		return true;
		
	}
	
	private int m_numColmn;
	private String m_filePath ;
	
	
}
