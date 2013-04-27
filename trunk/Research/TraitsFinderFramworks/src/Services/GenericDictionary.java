package Services;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class GenericDictionary<T>  implements IDictionary<T> ,Serializable
{

	Map<String,T> m_item ;
	Set<T> m_sortedItem;
	
	public  GenericDictionary()
	{
		m_item = new HashMap<String, T>();
		m_sortedItem =new  HashSet<T>();
	}
	
	
	@Override
	public void AddItem(String id, T item)
	{
		m_sortedItem.add(item);
		m_item.put(id, item);
		
	}

	@Override
	public T GetItem(int index) {
		return (T) ((GenericDictionary) m_item.values()).GetItem(index);
	}

	@Override
	public T GetItem(String Id) {
		return m_item.get(Id);
	}


	@Override
	public List<T> GetAllItem() {
		return (List<T>)m_item.values();
	}


	@Override
	public int Size() {
		return GetAllItem().size();
		
	}

	@Override
	public Object[] GetSortedItems() {
		return m_sortedItem.toArray();
				
	}

	@Override
	public boolean IsExist(String id) {
		
		return m_item.containsKey(id);
	}

	public Map ToMap()
	{
		return m_item;
	}
	
}
