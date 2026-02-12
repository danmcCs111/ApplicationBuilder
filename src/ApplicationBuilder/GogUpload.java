package ApplicationBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import Properties.LoggingMessages;
import Properties.PathUtility;
import Properties.StringUtility;

public class GogUpload 
{
	private static final String 
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
	private static final HashMap<String, String> textTagAndColumnName = new HashMap<String, String>();
	static {
		textTagAndColumnName.put("link", "GameUrl_Game_GameDatabase");
		textTagAndColumnName.put("title", "GameTitle_Game_GameDatabase");
		textTagAndColumnName.put("final-value", "GameFinalValue_GameCost_GameDatabase");
		textTagAndColumnName.put("base-value", "GameBaseValue_GameCost_GameDatabase");
	}
	
	public static void main(String [] args)
	{
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
					for(int i = 0; i < tags.size(); i++)
					{
						String tag = tags.get(i);
						String value = values.get(i);
						
						tag = StringUtility.stripChars(tag, stripCharsTag).strip();
						value = StringUtility.stripChars(value, stripCharsValue).strip();
						
						LoggingMessages.printOut(value + " " + tag + " " + textTagAndColumnName.get(tag));
					}
				}
			}
		}
	}
	
}
