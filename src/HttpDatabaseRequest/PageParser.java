package HttpDatabaseRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Properties.LoggingMessages;

public class PageParser
{
	public enum ParseAttribute
	{
		Title,
		Image
	}
	
	private static final String
		DELIMITER_FILTER_SEPERATOR = "@",
		DELIMITER_LIST_FILTER_SEPERATOR = "~";
			
	private String 
		titleLabel = "",
		domain = "";
	private HashMap<ParseAttribute, LinkedHashMap<String, ArrayList<String>>>
		pageMatchAndReplace = new HashMap<ParseAttribute, LinkedHashMap<String, ArrayList<String>>>();
	private String xmlString;
	
	public PageParser(String xmlString)
	{
		this.xmlString = xmlString;
	}
	
	public void setDomainMatch(String domain)
	{
		this.domain = domain;
	}
	
	public void addMatchAndReplace(ParseAttribute pa, String match, ArrayList<String> replacementStrips)
	{
		LinkedHashMap<String, ArrayList<String>> matchRepl = null;
		if(!pageMatchAndReplace.containsKey(pa))
		{
			matchRepl = new LinkedHashMap<String, ArrayList<String>>();
			matchRepl.put(match, replacementStrips);
		}
		else
		{
			matchRepl = pageMatchAndReplace.get(pa);
			matchRepl.put(match, replacementStrips);
		}
		pageMatchAndReplace.put(pa, matchRepl);
	}
	
	public LinkedHashMap<String, ArrayList<String>> getMatchAndReplace(ParseAttribute pa)
	{
		return pageMatchAndReplace.get(pa);
	}
	
	public void setTitleLabel(String titleLabel)
	{
		this.titleLabel = titleLabel;
	}
	
	public String getTitleLabel() 
	{
		return this.titleLabel;
	}
	
	public String getDomainMatch()
	{
		return this.domain;
	}
	
	public boolean isParser(String url)
	{
		if(url == null)
			return false;
		
		return (url.contains(domain));
	}
	
	public String getAttributeFromResponse(ParseAttribute pa, String response)
	{
		String ret = response;
		for(String key : pageMatchAndReplace.get(pa).keySet()) 
		{
			ret = stripToMatch(response, key);
			ret = replaceStrip(ret, pageMatchAndReplace.get(pa).get(key));
		}
		return ret;
	}
	
	public String getXmlString()
	{
		return constructXmlString();
	}
	
	private String constructXmlString()
	{
		String retStr = "";
		retStr += this.titleLabel + DELIMITER_FILTER_SEPERATOR;
		for(ParseAttribute pa : pageMatchAndReplace.keySet())
		{
			for(String mat : pageMatchAndReplace.get(pa).keySet())
			{
				retStr += mat + DELIMITER_FILTER_SEPERATOR;
				for(String repl : pageMatchAndReplace.get(pa).get(mat))
				{
					retStr += repl + DELIMITER_FILTER_SEPERATOR;
				}
				retStr += DELIMITER_LIST_FILTER_SEPERATOR;
			}
			retStr += DELIMITER_FILTER_SEPERATOR;
			retStr = retStr.replace(DELIMITER_LIST_FILTER_SEPERATOR+DELIMITER_FILTER_SEPERATOR,
					DELIMITER_FILTER_SEPERATOR);
		}
		retStr.subSequence(0, retStr.length()-1);
		
		LoggingMessages.printOut(retStr);
		
		return retStr;
	}
	
	private void readXmlString(String xmlString)
	{
		
	}
	
	public void setXmlString(String xmlString)
	{
//		this.xmlString = xmlString;
		//TODO.
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
	
}
