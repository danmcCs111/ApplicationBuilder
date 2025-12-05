package HttpDatabaseRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubePageParser 
{
	private static final String 
		YOUTUBE_PAGE_IMAGE = "yt3.googleusercontent.com";
//		YOUTUBE_TITLE = "\"><title>60 Minutes - YouTube</title>\"";
	
	public static String getImageUrl(String str)
	{
		 Pattern pattern = Pattern.compile(YOUTUBE_PAGE_IMAGE);
		 Matcher m = pattern.matcher(str);
		 return null;
	}
	
	public static boolean isYoutube(String url)
	{
		if(url == null)
			return false;
		
		return (url.contains("youtube.com"));
	}
}
