package ShapeWidgetComponents;

import java.awt.Shape;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


import Properties.LoggingMessages;

public class ShapeElement 
{
	public static final String ID_SPLITTER = "#";

	private String 
		nodeName,
		nodeId,
		parentNode;
	private ArrayList<String> attributes;
	private Shape shape = null;
	
	public ShapeElement(String nodeName, int count, ArrayList<String> attributes, String parentNode)
	{
		this.nodeName = nodeName;
		this.nodeId = nodeName + ID_SPLITTER + count;
		this.attributes = attributes;
		this.parentNode = parentNode;
		generateNodeClassObject();
	}
	
	private void generateNodeClassObject()
	{
		Class <?> c = null;
		Object o = null;
		try {
			c = Class.forName(this.nodeName);
			try {
				o = c.getDeclaredConstructor().newInstance();
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			shape = (Shape) o;
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		}
	}
	
	@Override
	public String toString()
	{
		return nodeId + ", " + parentNode + ", " + LoggingMessages.combine(attributes);
	}
}
