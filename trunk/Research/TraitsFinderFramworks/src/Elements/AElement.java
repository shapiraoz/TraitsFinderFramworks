package Elements;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import Core.CommonCBase;
import Core.Serialization.IElementSerializer;
import Elements.IElement;
import Services.Log.ELogLevel;

public abstract class AElement  extends CommonCBase implements Serializable{

	protected List<IElement> m_elements;
	
	protected List<IElementSerializer> m_serializerList;
	
	protected String m_name;
	
	public AElement()
	{
		m_elements = new LinkedList<IElement>();
		m_serializerList = new LinkedList<IElementSerializer>();
	}
	
	public AElement(String elementName )
	{
		m_elements = new LinkedList<IElement>();
		m_serializerList = new LinkedList<IElementSerializer>();
		m_name = elementName;
	}
	
	public void AddSerializer(IElementSerializer serializer) { m_serializerList.add(serializer);	}
	
	public void SetName(String elementName) {	m_name = elementName;}
	
	public String GetName() { 	return m_name; }
	
	public List<IElement> GetElements() { return m_elements; }

	public void AddElement(IElement element) { 	m_elements.add(element); }
	 
	public IElement[] GetSessionsArr() 	{ 	return m_elements.toArray(new IElement[0]); }

	
	public void SetProperty(String ProperyDef, Object ProperyData) {
		// TODO Auto-generated method stub
		
	}
	
	protected boolean ActivateSerialiers(boolean enableWrite)
	{
		boolean status =true;
		if (m_serializerList.size() == 0)
		{
			//WriteLineToLog("no serializer for saving process ", ELogLevel.ERROR);
			return false;
		}
		else
		{
			for (IElementSerializer serializer : m_serializerList)
			{
				status&= serializer.Save(enableWrite);
			}
		}
		return status;
	}
	
	protected boolean ActivateLinker(IElement element)
	{
		boolean status = true;
		if (m_serializerList.size()==0)
		{
			//WriteLineToLog("no serializers for linking process",ELogLevel.ERROR);
			return false;
		}
		
		for (IElementSerializer serializer : m_serializerList)
		{
			 status &= serializer.Link(element);
		}
		return status;
	}
		
	
}
