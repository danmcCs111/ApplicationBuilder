package HttpDatabaseRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubePageParser 
{
	private static final String 
		YOUTUBE_PAGE_IMAGE = "https://yt3.googleusercontent.com([^\"])*(\")",
		YOUTUBE_TITLE = "<title>([^<])*</title>";
	
	public static String getImageUrl(String response)
	{
		String ret = retFirstMatch(response, YOUTUBE_PAGE_IMAGE);
		return ret;
	}
	
	public static String getTitle(String response)
	{
		String ret = retFirstMatch(response, YOUTUBE_TITLE);
		return ret;
	}
	
	private static String retFirstMatch(String response, String pat)
	{
		Pattern pattern = Pattern.compile(pat);
		Matcher m = pattern.matcher(response);
		if(m.find())
		{
			return m.group();
		}
		else
		{
			return "";
		}
	}
	
	public static boolean isYoutube(String url)
	{
		if(url == null)
			return false;
		
		return (url.contains("youtube.com"));
	}
	
}
