package ApplicationBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Properties.LoggingMessages;
import Properties.PathUtility;

public class GogUpload 
{
	private static final String 
		gogFiles = PathUtility.getCurrentDirectory() + "/plugin-projects/SeleniumPython/gog/tmp/";
	private static final String []
			gogFileFilter = new String [] {"formatted-page", ".txt.tmp"};
	
	public static void main(String [] args)
	{
		ArrayList<String> files = PathUtility.getOSFileList(gogFiles, gogFileFilter);
		for(String s : files)
		{
			LoggingMessages.printOut(s);
			String fileContents = PathUtility.readFileToString(new File(gogFiles + s));
			fileContents = fileContents.substring(1, fileContents.length()-1) + ")"; //specific to page formatting ehh...
			
			String gameMatch = "\\([^\\)]*\\)";
			Pattern pattern = Pattern.compile(gameMatch);
			Matcher matcher = pattern.matcher(fileContents);
			while (matcher.find()) 
			{
	            LoggingMessages.printOut(matcher.group());
	        }
		}
	}
}
