package Core.Interfaces;
import Elements.*;
public interface ICrawler 
{

	IElement Crawl(boolean recursive);
	boolean SaveItem();

	
}
