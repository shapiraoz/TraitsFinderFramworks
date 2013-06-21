package Core.Serialization;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import Core.CommonCBase;
import Elements.IElement;
import Services.FileServices;
import Services.Log.ELogLevel;

public class ElementObjectSerializer extends CommonCBase implements IElementSerializer 
{

	

	public ElementObjectSerializer(IElement element , String filePath)
	{
		
		m_filePath = filePath;
		m_element = element;
		m_fos = null;
		m_outSerilazer = null;
		 
	}
	
	@Override
	public boolean Save(boolean close ) 
	{
		try
		{
			
			if (FileServices.PathExist(m_filePath))
			{
				WriteLineToLog("the file " +m_filePath +"is exist will replaced!!!", ELogLevel.WARNING);
				FileServices.DeleteFile(GetClassName(), m_filePath); 
			}
			
			Open(); // open for write
			
			if (m_element == null) 
			{
				WriteLineToLog("element is null!!!", ELogLevel.ERROR);
				return false;
			}
			WriteLineToLog("going to serilize " + m_element.GetName() + "elemnet", ELogLevel.INFORMATION);			
		    m_outSerilazer.writeObject(m_element);
			if (close)
			{
				m_fos.close();
				m_outSerilazer.close();
			}
			return true;
		}
		catch(Exception ex)
		{
			WriteLineToLog("Exception on element serialzer msg=" + ex.getMessage(), ELogLevel.ERROR);
			if (close) Close();
		}
		return false;
	}

	@Override
	public boolean Link(IElement elemet) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IElement Load()
	{
				
		if (m_element != null) 
		{
			WriteLineToLog("m_element is not null will replace values!!!",ELogLevel.WARNING );
		}
		FileInputStream fis =null;
		ObjectInputStream in = null;
		try
		{
			fis = new FileInputStream(m_filePath);
			in =  new ObjectInputStream (fis);
			Object t =  in.readObject();
			m_element =  (IElement)t;
			in.close();
			fis.close();
			
			if (m_element == null)
			{
				WriteLineToLog("load failed element is null", ELogLevel.ERROR);
				return null;
			}
			WriteLineToLog("the element " + m_element.GetName()+ " loaded ", ELogLevel.INFORMATION);
			return m_element;
		}
		catch (Exception ex)
		{
			WriteLineToLog("failed tp deserialize " +m_filePath+ " file exception msg=" + ex.getMessage()   , ELogLevel.ERROR);
			if (in != null)
				try { in.close();}
				catch (IOException e) {	}
			if (fis != null)
				try { fis.close();}
				catch (IOException e) {	}
			
		}
		
		return null;
}

	@Override
	public boolean Close() {
		if (m_fos != null )
		{
			try { m_fos.close(); }
			catch (IOException e) {}
		}
		if (m_outSerilazer != null)
		{
			try { m_outSerilazer.close();}
			catch (IOException e) {}
		}
		return true;
	}
	
	
	protected String m_filePath;
	protected IElement m_element;
	protected FileOutputStream m_fos = null;
	
	protected ObjectOutputStream m_outSerilazer = null;
	@Override
	public boolean Save() {
		
		return Save(true);
	}

	@Override
	public boolean Open()
	{
		try
		{
			if (!FileServices.PathExist(m_filePath)) 
			{
				WriteLineToLog("the file " + m_filePath + " not exist !!! will exit", ELogLevel.ERROR);
				
			}
			m_fos = new FileOutputStream(m_filePath);
			m_outSerilazer = new ObjectOutputStream(m_fos);
			return true;
		}
		catch (Exception e) 
		{
			WriteLineToLog("Exception!! fail to open seriaizers msg="+e.getMessage(), ELogLevel.ERROR);
		}
		
		return false;
	}

	@Override
	public boolean Close(boolean success) {
		
		return true;
	}

}

