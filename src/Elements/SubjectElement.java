package Elements;

import Core.CoreContext;
import Core.Serialization.ESerializerType;
import Core.Serialization.SerializerFactory;
import Elements.IElement;
import Services.Log.ELogLevel;

public class SubjectElement extends  EnumElement implements IElement
{
	
	public SubjectElement(String subjectName)
	{
		super(subjectName);
		if (m_serializer == null && CoreContext.SET_GRAPH)
		{
			WriteLineToLog("attach serializer", ELogLevel.INFORMATION);
			m_serializer = SerializerFactory.GetInstance().GetSerializer(ESerializerType.eNeo4J, this, CoreContext.GRAPH_DB_DIR);
		}
		AddProperty(EProperty.name.toString(), subjectName);
	}
	
	@Override
	public Object GetProperty(String ProperyDef) {
		
		if ( EProperty.valueOf(ProperyDef)==null)
		{
			WriteLineToLog("Error no "+ProperyDef+ "is exist ",ELogLevel.WARNING);
			return null;
		}
		return super.GetProperty(ProperyDef);
	}

	@Override
	public void Serialize() 
	{
		if ( m_serializer == null)
		{
			WriteLineToLog("no serializer for saving process ", ELogLevel.ERROR);	
			return;
		}
		m_serializer.Save();
		
		
	}

	@Override
	public void Link(IElement elment)
	{
		if (m_serializer==null)
		{
			WriteLineToLog("no serializer for linking process",ELogLevel.ERROR);
			return;
		}
		m_serializer.Link(elment);
		
	}

	
}
