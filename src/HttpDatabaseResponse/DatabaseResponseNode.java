package HttpDatabaseResponse;

import java.util.ArrayList;

import Properties.LoggingMessages;

public class DatabaseResponseNode 
{
	private String nodeName;
	private ArrayList<String> nodeAttributes;
	
	public DatabaseResponseNode(String nodeName, ArrayList<String> nodeAttributes)
	{
		this.nodeName = nodeName;
		this.nodeAttributes = nodeAttributes;
	}
	
	public String getNodeName()
	{
		return this.nodeName;
	}
	
	public ArrayList<String> getNodeAttributes()
	{
		return this.nodeAttributes;
	}
	
	@Override
	public String toString()
	{
		String att = (this.nodeAttributes != null && !this.nodeAttributes.isEmpty())
				? LoggingMessages.combine(this.nodeAttributes)
				: "";
		return this.nodeName + " " + att; 
				
	}
}
