package Elements;

import java.util.List;
import java.util.Map;

import Core.Serialization.IElementSerializer;

public interface IElement 
{
	List<IElement> GetElements();
	void AddElement(IElement element);
	void AddProperty(String ProperyDef,Object ProperyData);
	void AddSerializer(IElementSerializer serializer);
	void SetName(String elementName);
	Map GetProperties();
	boolean Serialize();
	boolean Link(IElement elment);
	String GetName();
	Object GetProperty(String ProperyDef);
		
}
