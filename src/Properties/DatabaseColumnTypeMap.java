package Properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import ApplicationBuilder.QueryUpdateTool;

public class DatabaseColumnTypeMap 
{
	private static String
		FIELD_COLUMN_DELIMITER = "@F@",
		DEFINITION_DELIMITER = "@D@";

	private LinkedHashMap<String, String>
		parseDbColumnDataType = new LinkedHashMap<String, String>();
	private ArrayList<HashMap<String, String>>
		parseFieldsAndValues = new ArrayList<HashMap<String, String>>();

	public DatabaseColumnTypeMap(String xml)
	{
		if(xml == null || xml.isBlank())
			return;
		
		String [] defs = xml.split(DEFINITION_DELIMITER);
		for(int i = 0; i < defs.length; i++)
		{
			LoggingMessages.printOut(defs[i]);
			String [] fieldColumn = defs[i].split(FIELD_COLUMN_DELIMITER);
			addFieldAndColumnDefinition(fieldColumn[0], fieldColumn[1]);
		}
	}
	
	public void addFieldAndColumnDefinition(String parseField, String column)
	{
		parseDbColumnDataType.put(parseField, column);
	}
	
	public LinkedHashMap<String, String> getParseFieldsAndDbColumn()
	{
		return parseDbColumnDataType;
	}
	
	public void addFieldValue(String parseField, String value, int index)
	{
		HashMap<String, String> parseFieldAndValues;
		if(index <= parseFieldsAndValues.size()-1)
		{
			parseFieldAndValues = parseFieldsAndValues.get(index);
		}
		else
		{
			parseFieldAndValues = new HashMap<String, String>();
			parseFieldsAndValues.add(parseFieldAndValues);
		}
		parseFieldAndValues.put(parseField, value);
	}
	
	public String getXmlString()
	{
		String retStr = "";
		
		int count = 0;
		for(String field : parseDbColumnDataType.keySet())
		{
			String column = parseDbColumnDataType.get(field);
			retStr += field + FIELD_COLUMN_DELIMITER + column;
			
			count++;
			if(count < parseDbColumnDataType.size())
			{
				retStr += DEFINITION_DELIMITER;
			}
		}
		LoggingMessages.printOut(retStr);
		return retStr;
	}
	
	private void getTableDefinition()
	{
		//TODO.
	}
	
	private void wrapValue()
	{
		//TODO.
	}
	
	public static void main(String [] args)
	{
		//only showing string.
		String ret = QueryUpdateTool.executeTableDefinition("Select * from encyclopedia.encyclopedia");
		LoggingMessages.printOut(ret);
	}
}
