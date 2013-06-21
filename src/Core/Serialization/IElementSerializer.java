package Core.Serialization;

import Elements.IElement;

public interface IElementSerializer
{
	boolean Open(); // just open to write
	boolean Save();
	boolean Save(boolean close);
	boolean Link(IElement elemet);
	IElement Load();
	boolean Close(); //close to write
	boolean Close(boolean success);
}
