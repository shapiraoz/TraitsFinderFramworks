package Core;

import Core.Interfaces.EUserDataType;
import Core.Interfaces.ISavedUserData;


public class UserDataFactory //extends AbstractFactory
{
	ISavedUserData GetInerface(EUserDataType type)
	{
		switch(type)
		{
			case eUserDataDictonary :
				return new UserDataDictionary();
		default:
			break;
		}
		return null;
	}
	
}
