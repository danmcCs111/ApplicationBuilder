package WidgetComponents;

import java.util.ArrayList;

import org.w3c.dom.Node;

import Properties.LoggingMessages;
import Properties.XmlNodeReader;

public class HtmlViewer extends XmlNodeReader 
{

	private static final String filename="tmp.html";
	
	@Override
	public Object createNewObjectFromNode(Node n, ArrayList<String> attributes, int counter, String parentNode) 
	{
		LoggingMessages.printOut(n.getNodeName());
		LoggingMessages.printOut(n.getNodeValue());
		LoggingMessages.printOut(attributes);
		return null;
	}

	@Override
	public String getFileTypeTitle() 
	{ 
		// TODO Auto-generated method stub
		return null;
	} 

	@Override
	public String getFileTypeFilter() 
	{
		// TODO Auto-generated method stub
		return "html";
	}

	@Override
	public String getDefaultDirectoryRelative() 
	{
		// TODO Auto-generated method stub
		return filename;
	}
	
	public static void main(String [] args)
	{
		HtmlViewer hv = new HtmlViewer();
		hv.readXml(filename);
	}

}
