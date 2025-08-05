package WidgetComponents;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import Actions.ScheduledCommand;
import Editors.ScheduledCommandEditor;
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
	
	public String toXmlFromEditorList(List<ScheduledCommandEditor> sces)//editor collection obj?
	{
		
//		String retStr = "";
//		retStr += "<" + SCHEDULED_COMMAND_PARENT_TAG + "> " + PathUtility.NEW_LINE; 
//		for(int i = 0; i < scs.size(); i++)
//		{
//			retStr += "<" + SCHEDULED_COMMAND_TAG;
//			ScheduledCommand sc = (ScheduledCommand) scs.get(i).getComponentValueObj();
//			retStr += sc.getXmlAttributesString() + " > ";
//			retStr += "</" + SCHEDULED_COMMAND_TAG + ">" + PathUtility.NEW_LINE;
//		}
//		retStr += "</" + SCHEDULED_COMMAND_PARENT_TAG + ">";
//		return retStr;
		
		ArrayList<ScheduledCommand> scs = new ArrayList<ScheduledCommand>();
		for(ScheduledCommandEditor sce : sces)
		{
			scs.add((ScheduledCommand) sce.getComponentValueObj());
		}
		return toXmlFromCommandList(scs);
	}
	
	public String toXmlFromCommandList(List<ScheduledCommand> scs)
	{
		String retStr = "";
		retStr += "<" + SCHEDULED_COMMAND_PARENT_TAG + "> " + PathUtility.NEW_LINE; 
		for(int i = 0; i < scs.size(); i++)
		{
			retStr += "<" + SCHEDULED_COMMAND_TAG;
			ScheduledCommand sc = (ScheduledCommand) scs.get(i);
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
