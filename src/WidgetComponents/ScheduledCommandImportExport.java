package WidgetComponents;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import Actions.ScheduledCommand;
import Properties.PathUtility;
import Properties.XmlNodeReader;

public class ScheduledCommandImportExport extends XmlNodeReader
{
	public static final String
		SCHEDULED_COMMAND_PARENT_TAG = "ScheduledCommands",
		SCHEDULED_COMMAND_TAG = "ScheduledCommand",
		FILE_TYPE_TITLE = "XML",
		FILE_TYPE_FILTER = "xml",
		DEFAULT_DIRECTORY_RELATIVE =  "/src/ApplicationBuilder/scheduledCommands/ ";
	
	public ScheduledCommandImportExport()
	{
		
	}
	
	public String toXml(List<ScheduledCommand> scs)
	{
		String retStr = "";
		retStr += "<" + SCHEDULED_COMMAND_PARENT_TAG + ">"; 
		for(int i = 0; i < scs.size(); i++)
		{
			retStr += "<" + SCHEDULED_COMMAND_TAG;
			ScheduledCommand sc = scs.get(i);
			retStr += sc.getXmlAttributesString() + " > ";
			retStr += "</" + SCHEDULED_COMMAND_TAG + ">" + PathUtility.NEW_LINE;
		}
		retStr += "</" + SCHEDULED_COMMAND_PARENT_TAG + ">"; 
		
		return retStr;
	}
	
	@Override
	public Object createNewObjectFromNode(Node n, ArrayList<String> attributes, int counter, String parentNode) 
	{
		return new ScheduledCommand(attributes);
	}

	@Override
	public String getFileTypeTitle() 
	{
		return FILE_TYPE_TITLE;
	}

	@Override
	public String getFileTypeFilter() 
	{
		return FILE_TYPE_FILTER;
	}

	@Override
	public String getDefaultDirectoryRelative() 
	{
		return DEFAULT_DIRECTORY_RELATIVE;
	}
	
}
