package Core.Serialization;

import Elements.IElement;

public interface IElementSerializer
{
	boolean Save();
	boolean Link(IElement elemet);
	IElement Load();
	boolean Close();
}
