package Core.Serialization;

import Core.CommonCBase;
import Elements.IElement;

public abstract class  ASerializer extends CommonCBase 
{
	
	protected IElement m_element;
	
	public ASerializer(IElement element)
	{
		m_element = element;
	}
}
