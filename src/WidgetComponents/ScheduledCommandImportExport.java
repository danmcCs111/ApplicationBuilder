package WidgetComponents;

import java.util.ArrayList;

import org.w3c.dom.Node;

import Actions.ScheduledCommand;
import Properties.XmlNodeReader;

public class ScheduledCommandImportExport extends XmlNodeReader
{
	private ScheduledCommand sc;
	
	public ScheduledCommandImportExport(ScheduledCommand sc)
	{
		this.sc = sc;
	}
	
	@Override
	public Object createNewObjectFromNode(Node n, ArrayList<String> attributes, int counter, String parentNode) 
	{
		return new ScheduledCommand(attributes);
	}
	
}
