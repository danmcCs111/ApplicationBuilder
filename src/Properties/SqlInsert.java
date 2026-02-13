package Properties;

import java.util.ArrayList;
import java.util.HashMap;

public class SqlInsert 
{
	private static final String 
		INSERT_STATMENT = 
			"INSERT INTO <arg0> (<arg1>)" +"\n" +
			"VALUES (<arg2>)";
	
	public static String buildIinsertStatement(ArrayList<HashMap<String, String>> columnAndValues)
	{
		HashMap<String, HashMap<String, String>> tableAndInsertStatement = new HashMap<String, HashMap<String, String>>();
		
		for(HashMap<String, String> rec : columnAndValues)
		{
			for(String column : rec.keySet())
			{
				String database = getDatabaseName(column);
				String table = getTableName(column);
				if(tableAndInsertStatement.containsKey(database+"."+table))
				{
					
				}
			}
		}
		return "";
	}
	
	public static String getDatabaseName(String columnName)
	{
		return columnName.split("_")[2];
	}
	
	public static String getTableName(String columnName)
	{
		return columnName.split("_")[1];
	}
}
