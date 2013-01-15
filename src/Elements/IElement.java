package Elements;

import java.util.List;
import java.util.Map;

import Core.Serialization.IElementSerializer;

public interface IElement 
{
	List<IElement> GetElements();
	void AddElement(IElement element);
	void AddProperty(String ProperyDef,Object ProperyData);
	void SetSerializer(IElementSerializer serializer);
	void SetName(String elementName);
	Map GetProperties();
	void Serialize();
	void Link(IElement elment);
	String GetName();
	Object GetProperty(String ProperyDef);
		
}
