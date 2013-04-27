package Core.Serialization;


import Elements.IElement;

public class SerializerFactory 
{
	private static SerializerFactory m_sFactory;
		
	private SerializerFactory ()
	{
		
	}
	
	public static SerializerFactory GetInstance()
	{
		if (m_sFactory ==null)
		{
			m_sFactory = new  SerializerFactory();
		}
		return m_sFactory;
	}
	
			
	public IElementSerializer AllocateSerializer(ESerializerType type,IElement element,String SerializerProperties)
	{
		switch (type) 
		{
		case eNeo4J:
			return new Neo4JSerializer(element,SerializerProperties);
		case eElemnetObj :
			return new ElementObjectSerializer(element , SerializerProperties);
		default:
			return null;
			
		}
	}
	
}
