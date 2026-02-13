package Properties;

import java.util.ArrayList;
import java.util.HashMap;

public class SqlInsert 
{
	private static final String 
		INSERT_STATMENT = 
			"INSERT INTO <arg0> (<arg1>)" +"\n" +
			"VALUES (<arg2>); " + "\n";
	
	public String buildIinsertStatement(ArrayList<HashMap<String, String>> columnAndValues)
	{
		String insertAccumulated = "";
		
		for(HashMap<String, String> rec : columnAndValues)
		{
			HashMap<String, HashMap<String, String>> tableAndInsertStatement = new HashMap<String, HashMap<String, String>>();
			
			for(String column : rec.keySet())
			{
				String value = rec.get(column);
				String database = getDatabaseName(column);
				String table = getTableName(column);
				String dbTable = database+"."+table;
				
				if(tableAndInsertStatement.containsKey(dbTable))
				{
					HashMap<String, String> columnAndValue = tableAndInsertStatement.get(dbTable);
					columnAndValue.put(column, value);
				}
				else
				{
					HashMap<String, String> columnAndValue = new HashMap<String, String>();
					columnAndValue.put(column, value);
					tableAndInsertStatement.put(dbTable, columnAndValue);
				}
			}
			
			String insert = createInserts(tableAndInsertStatement);
			insertAccumulated += insert;
		}
		return insertAccumulated;
	}
	
	private static String createInserts(HashMap<String, HashMap<String, String>> tableAndInsertStatement)
	{
		String inserts = "";
		for(String dbTable : tableAndInsertStatement.keySet())
		{
			HashMap<String, String> columnAndValues = tableAndInsertStatement.get(dbTable);
			String insert = INSERT_STATMENT.replace("<arg0>", dbTable);
			insert = createInsert(insert, columnAndValues);
			inserts += insert;
		}
		return inserts;
	}
	
	private static String createInsert(String insert, HashMap<String, String> columnAndValues)
	{
		String values = "";
		String columns = "";
		for(String column : columnAndValues.keySet())
		{
			String value = columnAndValues.get(column);
			values += value + ",";
			columns += column + ",";
		}
		values = values.substring(0, values.length()-1);
		columns = columns.substring(0, columns.length()-1);
		
		insert = updateInsert(insert, values, columns);
		
		return insert;
	}
	
	private static String updateInsert(String insert, String values, String columns)
	{
		insert = insert.replace("<arg1>", columns);
		insert = insert.replace("<arg2>", values);
		return insert;
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
