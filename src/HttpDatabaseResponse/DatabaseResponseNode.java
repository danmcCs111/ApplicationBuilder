package HttpDatabaseResponse;

import java.util.HashMap;

import Properties.LoggingMessages;

public class DatabaseResponseNode 
{
	public static final String 
		CLASS_TYPE_KEY = "classType",
		CONTENT_KEY = "content";
	private String nodeName;
	private HashMap<String, String> nodeAttributes;
	
	public DatabaseResponseNode(String nodeName, HashMap<String, String> nodeAttributes)
	{
		this.nodeName = nodeName;
		this.nodeAttributes = nodeAttributes;
	}
	
	public String getNodeName()
	{
		return this.nodeName;
	}
	
	public HashMap<String, String> getNodeAttributes()
	{
		return this.nodeAttributes;
	}
	
	@Override
	public String toString()
	{
		String att = (this.nodeAttributes != null && !this.nodeAttributes.isEmpty())
				? LoggingMessages.combine(this.nodeAttributes.values()) + " " + LoggingMessages.combine(this.nodeAttributes.keySet()) 
				: "";
		return this.nodeName + " " + att; 
	}
}
