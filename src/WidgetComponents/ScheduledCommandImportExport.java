package WidgetComponents;

import java.util.ArrayList;

import org.w3c.dom.Node;

import Actions.ScheduledCommand;
import Properties.XmlNodeReader;

public class ScheduledCommandImportExport extends XmlNodeReader
{
	public static final String 
		FILE_TYPE_TITLE = "XML",
		FILE_TYPE_FILTER = "xml",
		DEFAULT_DIRECTORY_RELATIVE =  "/src/ApplicationBuilder/scheduledCommands/ ";
	
	public ScheduledCommandImportExport()
	{
		
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
