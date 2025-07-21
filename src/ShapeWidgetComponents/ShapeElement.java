package ShapeWidgetComponents;

import java.util.ArrayList;

import Properties.LoggingMessages;

public class ShapeElement 
{
	private String 
		nodeId,
		parentNode;
	private ArrayList<String> attributes;
	
	public ShapeElement(String nodeId, ArrayList<String> attributes, String parentNode)
	{
		this.nodeId = nodeId;
		this.attributes = attributes;
		this.parentNode = parentNode;
	}
	
	@Override
	public String toString()
	{
		return nodeId + ", " + parentNode + ", " + LoggingMessages.combine(attributes);
	}
}
