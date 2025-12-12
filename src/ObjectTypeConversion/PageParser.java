package ObjectTypeConversion;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import ObjectTypeConversionEditors.PageParserEditor;
import Properties.LoggingMessages;
import WidgetComponentDialogs.PageParserDialog;

public class PageParser
{
	public enum ParseAttribute
	{
		Title,
		Image
	}
	
	private static final String
		QUOTE_REPLACEMENT = "@Q@",
		DELIMITER_FILTER_SEPERATOR = "@F@",
		DELIMITER_REPLACE_SEPERATOR = "@R@",
		DELIMITER_LIST_FILTER_SEPERATOR = "@L@";
			
	private String 
		titleLabel = "",
		domain = "";
	private HashMap<ParseAttribute, LinkedHashMap<String, ArrayList<String>>>
		pageMatchAndReplace = new HashMap<ParseAttribute, LinkedHashMap<String, ArrayList<String>>>();
	
	public PageParser(String xmlString)
	{
		readXmlString(xmlString);
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
	
	public ParseAttribute[] getParseAttributes()
	{
		return pageMatchAndReplace.keySet().toArray(new ParseAttribute[] {});
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
	
	private String constructXmlString()//TODO need json
	{
		String retStr = "";
		retStr += this.titleLabel + DELIMITER_FILTER_SEPERATOR;
		retStr += this.domain + DELIMITER_FILTER_SEPERATOR;
		for(ParseAttribute pa : pageMatchAndReplace.keySet())
		{
			retStr += pa.name() + DELIMITER_FILTER_SEPERATOR;
			for(String mat : pageMatchAndReplace.get(pa).keySet())
			{
				retStr += mat + DELIMITER_FILTER_SEPERATOR;
				for(String repl : pageMatchAndReplace.get(pa).get(mat))
				{
					retStr += repl + DELIMITER_REPLACE_SEPERATOR;
				}
				retStr = (String) retStr.subSequence(0, retStr.length()-DELIMITER_REPLACE_SEPERATOR.length());
				retStr += DELIMITER_LIST_FILTER_SEPERATOR;
			}
			retStr += DELIMITER_FILTER_SEPERATOR;
			retStr = retStr.replace(DELIMITER_LIST_FILTER_SEPERATOR+DELIMITER_FILTER_SEPERATOR,
					DELIMITER_FILTER_SEPERATOR);
		}
		retStr = (String) retStr.subSequence(0, retStr.length()-DELIMITER_FILTER_SEPERATOR.length());
		
		retStr = retStr.replaceAll("\"", QUOTE_REPLACEMENT);
		
		LoggingMessages.printOut(retStr);
		
		return retStr;
	}
	
	private void readXmlString(String xmlString)//TODO need json
	{
		if(xmlString == null || xmlString.isBlank())
			return;
		
		xmlString = xmlString.replaceAll(QUOTE_REPLACEMENT, "\"");
		
		String [] filterSepStr = xmlString.split(DELIMITER_FILTER_SEPERATOR);
		this.titleLabel = filterSepStr[0];
		this.domain = filterSepStr[1];
		for(int i = 2; i < filterSepStr.length; i++)
		{
			String match = filterSepStr[i];
			LinkedHashMap<String, ArrayList<String>> filter = new LinkedHashMap<String, ArrayList<String>>();
			if(i+1 < filterSepStr.length)
			{
				i++;
				String parseType = null;
				String [] replaces = filterSepStr[i].split(DELIMITER_REPLACE_SEPERATOR);
				parseType = replaces[0];
				replaces = filterSepStr[i+1].split(DELIMITER_REPLACE_SEPERATOR);
				
				filter.put(parseType, new ArrayList<String>());
				LoggingMessages.printOut(replaces.length + " " + parseType);
				if(replaces.length > 1)
				{
					for(int j = 0; j < replaces.length; j++)
					{
						filter.get(parseType).add(replaces[j]);
					}	
				}
				else
				{
					filter.get(parseType).add(filterSepStr[i+1]);
					i++;
				}
				LoggingMessages.printOut(match);
				pageMatchAndReplace.put(ParseAttribute.valueOf(match), filter);
			}
		}
	}
	
	public void setXmlString(String xmlString)
	{
		readXmlString(xmlString);
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
	
	public static void main(String [] args)
	{
		PageParser youtube = new PageParser("Youtube@F@youtube.com@F@Image@F@https://yt3.googleusercontent.com([^@Q@])*(@Q@)@F@@Q@@F@Title@F@<title>([^<])*</title>@F@<title>@R@</title>@R@[^a-zA-Z0-9\\-\\s]");
		JFrame f = new JFrame();
		PageParserEditor ppe = new PageParserEditor();
		f.add(ppe);
		PageParserDialog ppd = new PageParserDialog(ppe, youtube);
		ppd.setVisible(true);
		ppd.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				ppd.getPageParser().getXmlString();
			}
		});
		
	}
}
