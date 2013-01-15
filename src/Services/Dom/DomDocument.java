package Services.Dom;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Services.FileServices;
import Services.Log.ELogLevel;

import Core.CommonCBase;

public class DomDocument extends CommonCBase
{
	
	
	private Document m_document = null; 
	private XPath m_xpath;
	
	public DomDocument(String xmlFilePath) throws ParserConfigurationException, SAXException, IOException
	{
		if (FileServices.PathExist(xmlFilePath))
		{
			File xmlPath  = new File(xmlFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			m_document =  dBuilder.parse(xmlPath);
			XPathFactory factory = XPathFactory.newInstance();
			m_xpath = factory.newXPath();
		}
		
	}
	
	public Document GetDocument()
	{
		return m_document;
	}
	
	public Document GetDoucument(String xPathStr)
	{
		if (m_document != null && m_xpath!=null)
		{
			
			try {
				return  (Document)m_xpath.evaluate(xPathStr, m_document,XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				WriteLineToLog("error can't get node according to xpath "+ xPathStr+ " error:" +e.getMessage(),ELogLevel.ERROR);
				
			}
		}
		else
		{
			WriteLineToLog("error document and xpath art not initialize",ELogLevel.ERROR);
		}
		return null;
	}
	
	public Node GetNode(String xPathStr)
	{
		if (m_document != null && m_xpath!=null)
		{
			
			try {
				return  (Node)m_xpath.evaluate(xPathStr, m_document,XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				WriteLineToLog("error can't get node according to xpath "+ xPathStr+ " error:" +e.getMessage(),ELogLevel.ERROR);
				
			}
		}
		else
		{
			WriteLineToLog("error document and xpath art not initialize",ELogLevel.ERROR);
		}
		return null;
	}
	
	public NodeList GetNodes(String xPathStr)
	{
		if (m_document != null && m_xpath!=null)
		{
			
			try {
				return  (NodeList)m_xpath.evaluate(xPathStr, m_document,XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				WriteLineToLog("error can't get node according to xpath "+ xPathStr+ " error:" +e.getMessage(),ELogLevel.ERROR);
				
			}
		}
		else
		{
			WriteLineToLog("error document and xpath art not initialize",ELogLevel.ERROR);
		}
		return null;
	}
	
		
	
	
}
