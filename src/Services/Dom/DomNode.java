package Services.Dom;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Services.Log.ELogLevel;

import Core.CommonCBase;

public class DomNode extends CommonCBase
{
	
	protected XPathFactory m_factory;
	protected Node m_node;
	protected static XPath m_xpath;
	
	public  DomNode()
	{
		m_node = null;
		Init();
	}
	
	public DomNode(Node node)
	{
		m_node = node;
		Init();
	}
	
	private void Init()
	{
		m_factory = XPathFactory.newInstance();
		m_xpath = m_factory.newXPath();
	}
	
	public Node GetNode()
	{
		return m_node;
	}
	
	public DomNode GetDomNode()
	{
		return new DomNode(GetNode());
	}
	
	public Node GetNode(String path)
	{
		try 
		{
			return (Node) m_xpath.evaluate(path, m_node,XPathConstants.NODE);
		} catch (XPathExpressionException e) 
		{
			PrintError(e);
			return null;
		}
	}
	
	public DomNode GetDomNode(String path)
	{
		return new DomNode(GetNode(path));
	}
	
	public String GetValue()
	{
		if (m_node ==null) return "";
		else
		{
			try
			{
				return 	m_node.getNodeValue();
			}
			catch (Exception e)
			{
				WriteLineToLog("error while try to get node value :  " + e.getMessage(),ELogLevel.ERROR);
				return "";

			}
		}
	}
	
		
	public String GetAttribute(String attribute)
	{
		if (m_node ==null) return "";
		else
		{
			try
			{
				return m_node.getAttributes().getNamedItem(attribute).getNodeValue();	
			}
			catch (Exception e)
			{
				WriteLineToLog("error while try to get node " +attribute +" attribute  :  " + e.getMessage(),ELogLevel.ERROR);
				return "";
			}
					
		}
	}
	
	public String GetValue(Node node) throws Exception
	{
		if (node == null) return "";
		else
		{
			return 	node.getNodeValue();
		}
	}
	
	public String getValue(DomNode domNode) throws Exception 
	{
		return GetValue(domNode.GetNode());
	}
	
	public String GetAttribute(Node node ,String attribute) throws Exception
	{
		try
		{
			if (node ==null) return "";
			else
			{
				return node.getAttributes().getNamedItem(attribute).getNodeValue();	
			}
		}
		catch (Exception e) 
		{
			//WriteLineToLog("attribute not exist", ELogLevel.WARNING);
			return "";
		}
	}
	
	public String GetAttribute(DomNode domNode ,String attribute) throws Exception
	{
		return GetAttribute(domNode.GetNode(), attribute);
	}
	
	public DomNode GetDomNode(String path,Node node) throws XPathExpressionException
	{
		return new DomNode(GetNode(path,node));
	}
	
	public DomNode GetDomNode(String path ,DomNode node) throws XPathExpressionException
	{
		return GetDomNode(path,node.GetNode());
	}
		
	public  Node GetNode(String path,Node node) throws XPathExpressionException
	{
		return (Node) m_xpath.evaluate(path, node,XPathConstants.NODE);
	}
	
	public Node GetNode(String path , DomNode node) throws XPathExpressionException
	{
		return GetNode(path,node.GetNode());
	}
	
	public  Node GetNode(String path,Document doc) throws XPathExpressionException
	{
		return (Node) m_xpath.evaluate(path, doc,XPathConstants.NODE);
	}
	 
	public Node GetNode(String path,DomDocument doc) throws XPathExpressionException
	{
		return GetNode(path , doc.GetDocument());
	}
	
	public DomNode GetDomNode (String path,DomDocument doc) throws XPathExpressionException
	{
		return new DomNode(GetNode(path,doc));
	}
	
	public DomNode GetDomNoe(String path ,Document doc) throws XPathExpressionException
	{
		return new DomNode(GetNode(path,doc));
	}
	
	public NodeList GetNodeList(String path)
	{
		try
		{
			return (NodeList) m_xpath.evaluate(path, m_node, XPathConstants.NODESET);
		} 
		catch (XPathExpressionException e)
		{
			WriteLineToLog(e.getMessage(),ELogLevel.ERROR);
			return null;
		}
	}
	
	
		
	public  NodeList GetNodeList(String path,Document doc) throws XPathExpressionException
	{
		return (NodeList) m_xpath.evaluate(path,doc ,XPathConstants.NODESET);
	}
	
	public  NodeList GetNodeList(String path,Node node) throws XPathExpressionException
	{
		return (NodeList) m_xpath.evaluate(path,node ,XPathConstants.NODESET);
	}
		
	private void PrintError(Exception e)
	{
		WriteLineToLog("Error: " +e.getMessage(),ELogLevel.ERROR);
		WriteLineToLog(e.getStackTrace().toString(),ELogLevel.ERROR);
		
	}
	
}
