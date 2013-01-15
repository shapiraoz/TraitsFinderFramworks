package Services;

import java.util.List;

public interface IDictionary<T>
{
		
	void AddItem(String id, T item);
	
	T GetItem(int index);
	
	T GetItem(String Id);
	
	List<T> GetAllItem();
	
	int Size();
	
	Object[] GetSortedItems();
	
	boolean IsExist(String id);
	
}
