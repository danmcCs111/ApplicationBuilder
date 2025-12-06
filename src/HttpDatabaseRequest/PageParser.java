package HttpDatabaseRequest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageParser
{
	private String 
		domain = "";
		
	private LinkedHashMap<String, ArrayList<String>>
		pageImageMatchAndReplace = new LinkedHashMap<String, ArrayList<String>>(),
		pageTitleMatchAndReplace = new LinkedHashMap<String, ArrayList<String>>();
	
	public void setDomainMatch(String domain)
	{
		this.domain = domain;
	}
	
	public void addImageMatchAndReplace(String imageMatch, ArrayList<String> replacementStrips)
	{
		pageImageMatchAndReplace.put(imageMatch, replacementStrips);
	}
	
	public void addTitleMatchAndReplace(String imageMatch, ArrayList<String> replacementStrips)
	{
		pageTitleMatchAndReplace.put(imageMatch, replacementStrips);
	}

	public String getImageUrl(String response)
	{
		String ret = response;
		for(String key : pageImageMatchAndReplace.keySet()) 
		{
			ret = stripToMatch(response, key);
			ret = replaceStrip(ret, pageImageMatchAndReplace.get(key));
		}
		return ret;
	}
	
	public String getTitle(String response)
	{
		String ret = response;
		for(String key : pageTitleMatchAndReplace.keySet()) 
		{
			ret = stripToMatch(response, key);
			ret = replaceStrip(ret, pageTitleMatchAndReplace.get(key));
		}
		return ret;
	}
	
	private String stripToMatch(String response, String pat)
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
	
	private String replaceStrip(String response, ArrayList<String> replaces)
	{
		String retStr = response;
		for(String repl : replaces)
		{
			retStr = retStr.replaceAll(repl, "");
		}
		return retStr;
	}
	
	public boolean isParser(String url)
	{
		if(url == null)
			return false;
		
		return (url.contains(domain));
	}
}
