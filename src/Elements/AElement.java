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
	
	protected void ActivateSerialiers(boolean enableWrite)
	{
		if (m_serializerList.size() == 0)
		{
			WriteLineToLog("no serializer for saving process ", ELogLevel.ERROR);
			return ;
		}
		else
		{
			for (IElementSerializer serializer : m_serializerList)
			{
				serializer.Save(enableWrite);
			}
		}
	}
	
	protected void ActivateLinker(IElement element)
	{
		if (m_serializerList.size()==0)
		{
			WriteLineToLog("no serializers for linking process",ELogLevel.ERROR);
			return;
		}
		
		for (IElementSerializer serializer : m_serializerList)
		{
			serializer.Link(element);
		}
	}
		
	
}
