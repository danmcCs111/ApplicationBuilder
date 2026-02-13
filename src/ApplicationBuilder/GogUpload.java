package ApplicationBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import HttpDatabaseRequest.HttpDatabaseRequest;
import Properties.LoggingMessages;
import Properties.PathUtility;
import Properties.StringUtility;

public class GogUpload 
{
	private static final String 
		ENDPOINT = "http://localhost:",
		REQUEST_TYPE_HEADER_KEY = "Get-request-type";
	private static final int
		PORT_NUMBER = 8000;

	private static final String 
		primaryKey = "title",
		gameMatch = "\\([^\\)]*\\)",
		valueAndTagMatch = "[^\\|\\(]*\\|[^\\|]*\\|",
		tagMatch = "\\|[^\\|]*\\|",
		valueMatch = "[^\\|]*\\|",
		gogFiles = PathUtility.getCurrentDirectory() + "/plugin-projects/SeleniumPython/gog/tmp/";
	private static char []
		stripCharsValue = new char[] {',', '|', '$'},
		stripCharsTag = new char[] {',', '|'};
	private static final String []
		gogFileFilter = new String [] {"formatted-page", ".txt.tmp"};
	private static final HashMap<String, String>
		textTagAndColumnName = new HashMap<String, String>();
	static {
		textTagAndColumnName.put("link", "GameUrl_Game_GameDatabase");
		textTagAndColumnName.put("title", "GameTitle_Game_GameDatabase");
		textTagAndColumnName.put("final-value", "GameFinalValue_GameCost_GameDatabase");
		textTagAndColumnName.put("base-value", "GameBaseValue_GameCost_GameDatabase");
	}
	
	public GogUpload()
	{
		
	}
	
	public void queryDatabase(String request)
	{
		HttpDatabaseRequest.executeGetRequest(
			ENDPOINT, 
			PORT_NUMBER, 
			request, 
			REQUEST_TYPE_HEADER_KEY, 
			"Query");
	}
	
	public ArrayList<HashMap<String, String>> getUploadFromFiles()
	{
		ArrayList<HashMap<String, String>> primaryKeyTagAndValue = new ArrayList<HashMap<String, String>>();
		
		ArrayList<String> files = PathUtility.getOSFileList(gogFiles, gogFileFilter);
		for(String s : files)
		{
			LoggingMessages.printOut(s);
			String fileContents = PathUtility.readFileToString(new File(gogFiles + s));
			fileContents = fileContents.substring(1, fileContents.length()-1) + ")"; //specific to page formatting ehh...
			
			ArrayList<String> games = StringUtility.getMatches(fileContents, gameMatch);
			for(String game : games)
			{
				ArrayList<String> gameValues = StringUtility.getMatches(game, valueAndTagMatch);
				for(String gameValue : gameValues)
				{
					ArrayList<String> tags = StringUtility.getMatches(gameValue, tagMatch);
					ArrayList<String> values = StringUtility.getMatches(gameValue, valueMatch);
					HashMap<String, String> tagAndValue = new HashMap<String, String>();
					
					for(int i = 0; i < tags.size(); i++)
					{
						String tag = tags.get(i);
						String value = values.get(i);
						
						tag = StringUtility.stripChars(tag, stripCharsTag).strip();
						value = StringUtility.stripChars(value, stripCharsValue).strip();
						
						tagAndValue.put(tag, value);
					}
					primaryKeyTagAndValue.add(tagAndValue);
				}
			}
		}
		return primaryKeyTagAndValue;
	}
	
	public static void main(String [] args)
	{
		GogUpload gog = new GogUpload();
		ArrayList<HashMap<String, String>> primaryKeyTagAndValue = gog.getUploadFromFiles();
		for(int i = 0; i <primaryKeyTagAndValue.size(); i++)
		{
			for(String tag : primaryKeyTagAndValue.get(i).keySet())
			{
				String value = primaryKeyTagAndValue.get(i).get(tag);
				String columnName = textTagAndColumnName.get(tag);
				LoggingMessages.printOut(value + " " + tag + " " + columnName);
			}
		}
		String databaseName = StringUtility.getDatabaseName("GameTitle_Game_GameDatabase");
		String tableName = StringUtility.getTableName("GameTitle_Game_GameDatabase");
		
//		databaseName = "VideoDatabase";
//		tableName = "videoYoutube";
		
		gog.queryDatabase("Select * from " + databaseName + "." + tableName + ";");
	}
	
}
