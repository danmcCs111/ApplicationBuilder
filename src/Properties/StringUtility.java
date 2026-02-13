package Properties;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface StringUtility 
{
	public static ArrayList<String> getMatches(String textToMatch, String regexMatch)
	{
		ArrayList<String> values = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile(regexMatch);
		Matcher matcher = pattern.matcher(textToMatch);
		while (matcher.find()) 
		{
			String value = matcher.group();
			values.add(value);
		}
		return values;
	}
	
	public static String stripChars(String text, char ... charactersToStrip)
	{
		for(char c : charactersToStrip)
		{
			String s = c + "";
			text = text.replace(s, "");
		}
		return text;
	}
	
}
