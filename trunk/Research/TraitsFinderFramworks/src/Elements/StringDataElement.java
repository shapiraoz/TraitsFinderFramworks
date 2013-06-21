
package Elements;

import Core.Serialization.IElementSerializer;

import Services.GenericDictionary;
import Services.Log.ELogLevel;



import java.util.Map;



public class StringDataElement extends AElement implements IElement {

	
	private GenericDictionary<String> m_properties;

	public StringDataElement()
	{
		super();
		m_properties =new GenericDictionary<String>();
	}
	
	public StringDataElement(String ProperyDef, String ProperyData)
	{
		if (ProperyDef == EProperty.name.toString())
		{
			m_name = ProperyData;
		}
		m_properties = new GenericDictionary<String>();
		m_properties.AddItem(ProperyDef, ProperyData);
	}
			
	
	public void AddProperty(String ProperyDef, Object ProperyData)
	{
		super.SetProperty(ProperyDef, ProperyData);
		m_properties.AddItem(ProperyDef,  ProperyData.toString());
	}

		
	public void SetProperty(String ProperyDef, String ProperyData)
	{
		super.SetProperty(ProperyDef, ProperyData);
		SetProperty(ProperyDef,(String)ProperyData);
	}
	
	

	public Object GetProperty(String ProperyDef) {
		
		if (!m_properties.IsExist(ProperyDef))
		{
			WriteLineToLog("the property " + ProperyDef +" is not exist ",ELogLevel.WARNING);
			return null;
		}
		return m_properties.GetItem(ProperyDef);		
	}


	public Map GetProperties() 
	{
		return m_properties.ToMap();
	}

	

	@Override
	public boolean Link(IElement elment) {
		return super.ActivateLinker(elment);
		
	}

	@Override
	public boolean Serialize() {
		//move to AElement !!
		/*
				if (m_serializerList.size() == 0)
				{
					WriteLineToLog("no serializer for saving process ", ELogLevel.ERROR);
					return ;
				}
				else
				{
					for (IElementSerializer serializer : m_serializerList)
					{
						serializer.Save();
					}
				}
		*/
		return  super.ActivateSerialiers(true);
	}


	
	
}
