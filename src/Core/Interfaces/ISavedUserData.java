package Core.Interfaces;

import Elements.*;

public interface ISavedUserData 
{
	void AddUser(String userName);
	
	void AddUser(String userName, String property);
	
	String  GetUserProperty(String user);
	
	void AddElementToUser(String User,IElement element);
	
	IElement GetUserElemet(String user);
	
	int GetUserCont();
}
