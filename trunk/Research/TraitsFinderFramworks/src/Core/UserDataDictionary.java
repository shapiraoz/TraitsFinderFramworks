package Core;

import Core.Interfaces.ISavedUserData;
import Elements.IElement;
import Services.GenericDictionary;

public class UserDataDictionary implements ISavedUserData
{

	GenericDictionary<String> m_users; //url
	GenericDictionary<IElement> m_usersElement; //elements
	
	public UserDataDictionary()
	{
		m_users = new GenericDictionary<String>();
		m_usersElement = new GenericDictionary<IElement>();
	}
	
	@Override
	public void AddUser(String userName) {
		m_users.AddItem(userName, CoreContext.EMPTY);
		
	}

	@Override
	public void AddUser(String userName, String property) {
		m_users.AddItem(userName,property);
		
	}

	@Override
	public String GetUserProperty(String user) 
	{
		return m_users.GetItem(user);
	}

	@Override
	public void AddElementToUser(String user, IElement element) {
			
		m_usersElement.AddItem(user, element);
	}

	@Override
	public IElement GetUserElemet(String user) {
		return  m_usersElement.GetItem(user);
		
	}

	@Override
	public int GetUserCont() {
		return m_users.Size();
	}

}
