package WidgetUtility;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ApplicationBuilder.LoggingMessages;
import Params.ParameterEditor;
import Params.XmlToWidgetGenerator;

public class EditorToXml 
{
	private static final String 
		CLOSE_BRACKET_OPEN = "</",
		CLOSE_BRACKET_CLOSE = ">",
		OPEN_BRACKET_OPEN = "<",
		OPEN_BRACKET_CLOSE = ">",
		TAB_CHAR = "\t",
		BUILD_OPEN_TAG = "<Build>",
		BUILD_CLOSE_TAG = "</Build>";
	
	public static final HashMap<String, String> xmlWriteReplace = new HashMap<String, String>();
	static {
		xmlWriteReplace.put(">", "&gt;");
		xmlWriteReplace.put("<", "&lt;");
	}
		
	public static void writeXml(String sourceFile, List<WidgetCreatorProperty> widgetCreatorProperties)
	{
		if(widgetCreatorProperties == null || widgetCreatorProperties.isEmpty())
		{
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(BUILD_OPEN_TAG + System.lineSeparator());
		
		String 
			lastParentRefWithID = "",
			lastRef = "";
		ArrayList<String> parentRefs = new ArrayList<String>();
		int tabcount = 0;
		
		for(WidgetCreatorProperty wcp : widgetCreatorProperties)
		{
			LoggingMessages.printOut("" + tabcount);
			
			if(!lastParentRefWithID.equals(wcp.getParentRefWithID()) && parentRefs.contains(wcp.getParentRefWithID()))
			{
				for(int i =0; i < tabcount; i++)
				{
					sb.append(TAB_CHAR);
				}
				sb.append(CLOSE_BRACKET_OPEN + lastRef + CLOSE_BRACKET_CLOSE + System.lineSeparator());//close out tag
				
				do
				{
					tabcount--;
					for(int i =0; i < tabcount; i++)
					{
						sb.append(TAB_CHAR);
					}
					sb.append(CLOSE_BRACKET_OPEN + WidgetCreatorProperty.stripParentRefWithID(parentRefs.get(
							parentRefs.size()-1)) + CLOSE_BRACKET_CLOSE + System.lineSeparator());//close out tag
					parentRefs.remove(parentRefs.size()-1);
				} while(!lastParentRefWithID.equals(wcp.getParentRefWithID()) && 
						!parentRefs.get(parentRefs.size()-1).equals(wcp.getParentRefWithID()));
				
				for(int i =0; i < tabcount; i++)
				{
					sb.append(TAB_CHAR);
				}
				
			}
			
			else if(lastParentRefWithID.equals(wcp.getParentRefWithID()))
			{
				for(int i =0; i < tabcount; i++)
				{
					sb.append(TAB_CHAR);
				}
				sb.append(CLOSE_BRACKET_OPEN + lastRef + CLOSE_BRACKET_CLOSE + System.lineSeparator());
				for(int i =0; i < tabcount; i++)
				{
					sb.append(TAB_CHAR);
				}	
			}
			else
			{
				tabcount++;
				for(int i =0; i < tabcount; i++)
				{
					sb.append(TAB_CHAR);
				}
			}
			
			sb.append(OPEN_BRACKET_OPEN + wcp.getRef() + " ");
			for(XmlToWidgetGenerator xwg : wcp.getXmlToWidgetGenerators())
			{
				String metName = xwg.getMethodName();
				
				String parWrite = "";
				for(int i = 0; i < xwg.getParameterEditors().size(); i++)
				{
					ParameterEditor pe = xwg.getParameterEditors().get(i);
					if(pe != null)
					{
						if(parWrite.isBlank())
							parWrite += LoggingMessages.combine(pe.getComponentValue());
						else
							parWrite += ", " + LoggingMessages.combine(pe.getComponentValue());
					}
					else
					{
						List<String> params = xwg.getParamsList().get(i);
						if(parWrite.isBlank())
							parWrite += LoggingMessages.combine(params);
						else
							parWrite += ", " + LoggingMessages.combine(params);
					}
					
				}
				for(String replChar : xmlWriteReplace.keySet())
				{
					if(parWrite.contains(replChar))
					{
						String repl = xmlWriteReplace.get(replChar);
						parWrite = parWrite.replaceAll(replChar, repl);
					}
				}
				sb.append(metName + "=\"" + parWrite + "\" ");
			}
			sb.append(OPEN_BRACKET_CLOSE + System.lineSeparator());
			lastParentRefWithID = wcp.getParentRefWithID() == null ? lastParentRefWithID : wcp.getParentRefWithID();
			lastRef = wcp.getRef();
			if(!parentRefs.contains(lastParentRefWithID))
			{
				parentRefs.add(lastParentRefWithID);
			}
		}
		
		for(int i =0; i < tabcount; i++)
		{
			sb.append(TAB_CHAR);
		}
		sb.append(CLOSE_BRACKET_OPEN + lastRef + CLOSE_BRACKET_CLOSE + System.lineSeparator());//close out tag
		
		while(parentRefs.size() > 1)
		{
			tabcount--;
			for(int i =0; i < tabcount; i++)
			{
				sb.append(TAB_CHAR);
			}
			sb.append(CLOSE_BRACKET_OPEN + WidgetCreatorProperty.stripParentRefWithID(
					parentRefs.get(parentRefs.size()-1)) + CLOSE_BRACKET_CLOSE + System.lineSeparator());//close out tag
			parentRefs.remove(parentRefs.size()-1);
		} 
		
		sb.append(BUILD_CLOSE_TAG + System.lineSeparator());
		
		LoggingMessages.printOut(sb.toString());
		
		try {
			FileWriter fw = new FileWriter(sourceFile);
			fw.write(sb.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
