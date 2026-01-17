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
	public static final String
		PARSER_DELIMIT_COLLECTION = "@C@",
		QUOTE_REPLACEMENT = "@Q@",
		DELIMITER_FILTER_SEPERATOR = "@F@",
		DELIMITER_MATCH_FILTER_SEPERATOR = "@M@",
		DELIMITER_REPLACE_SEPERATOR = "@R@",
		DELIMITER_REPLACE_VALUE_SEPERATOR = "@RV@",
		DELIMITER_LIST_FILTER_SEPERATOR = "@L@";
			
	private String 
		titleLabel = "",
		domain = "";
	private HashMap<ParseAttribute, LinkedHashMap<String, ArrayList<String[]>>>
		pageMatchAndReplace = new HashMap<ParseAttribute, LinkedHashMap<String, ArrayList<String[]>>>();
	
	public PageParser()
	{
		
	}
	
	public PageParser(String xmlString)
	{
		readXmlString(xmlString);
	}
	
	public void setDomainMatch(String domain)
	{
		this.domain = domain;
	}
	
	public void addBlankMatchAndReplace(ParseAttribute pa)
	{
		LinkedHashMap<String, ArrayList<String[]>> matchRepl = new LinkedHashMap<String, ArrayList<String[]>>();
		pageMatchAndReplace.put(pa, matchRepl);
	}
	
	public void addMatchAndReplace(ParseAttribute pa, String match, ArrayList<String[]> replacementStrips)
	{
		LinkedHashMap<String, ArrayList<String[]>> matchRepl = null;
		if(!pageMatchAndReplace.containsKey(pa))
		{
			matchRepl = new LinkedHashMap<String, ArrayList<String[]>>();
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
	
	public LinkedHashMap<String, ArrayList<String[]>> getMatchAndReplace(ParseAttribute pa)
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
	
	public String [] getAttributesFromResponse(ParseAttribute pa, String response, boolean singleMatch)
	{
		if(!pageMatchAndReplace.containsKey(pa))
			return null;
		
		String [] attributes = null;
		int count = 1;
		for(String key : pageMatchAndReplace.get(pa).keySet()) 
		{
			if(count == 1)
			{
				attributes = stripToMatch(response, key, singleMatch);
				attributes = replaceStrip(pageMatchAndReplace.get(pa).get(key), attributes);
			}
			else
			{
				for(int i = 0; i < attributes.length; i++)
				{
					attributes[i] = stripToMatch(attributes[i], key, true)[0];
					attributes[i] = replaceStrip(pageMatchAndReplace.get(pa).get(key), attributes[i])[0];
				}
			}
			count++;
		}
		if(attributes.length != 0)
			LoggingMessages.printOut(attributes[0]);
		return attributes;
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
				retStr += DELIMITER_MATCH_FILTER_SEPERATOR + mat + DELIMITER_REPLACE_SEPERATOR;
				for(String [] repl : pageMatchAndReplace.get(pa).get(mat))
				{
					retStr += repl[0] + DELIMITER_REPLACE_VALUE_SEPERATOR + repl[1] + DELIMITER_REPLACE_SEPERATOR;
				}
				if(pageMatchAndReplace.get(pa).get(mat).size() >= 1)
				{
					retStr = (String) retStr.subSequence(0, retStr.length()-DELIMITER_REPLACE_SEPERATOR.length());
				}
			}
			retStr += DELIMITER_LIST_FILTER_SEPERATOR;
			retStr += DELIMITER_FILTER_SEPERATOR;
			retStr = retStr.replace(DELIMITER_LIST_FILTER_SEPERATOR+DELIMITER_FILTER_SEPERATOR,
					DELIMITER_FILTER_SEPERATOR);
		}
		retStr = (String) retStr.subSequence(0, retStr.length()-DELIMITER_FILTER_SEPERATOR.length());
		
		retStr = retStr.replaceAll("\"", QUOTE_REPLACEMENT);
		
		LoggingMessages.printOut(retStr);
		
		return retStr;
	}
	
	/**
	 * title,domain,parsetype,[matches&replaces]
	 * @param xmlString
	 */
	private void readXmlString(String xmlString)//TODO need json
	{
		ParseAttributes pas = new ParseAttributes(getParseAttributes());
		
		if(xmlString == null || xmlString.isBlank())
		{
			pas.addAttribute(new ParseAttribute("Title"));//Default
			pas.addAttribute(new ParseAttribute("Image"));
			for(ParseAttribute pa : pas.parseAttributes)
			{
				pageMatchAndReplace.put(pa, new LinkedHashMap<String, ArrayList<String[]>>());
			}
			return;
		}
		
		xmlString = xmlString.replaceAll(QUOTE_REPLACEMENT, "\"");
		
		String [] filterSepStr = xmlString.split(DELIMITER_FILTER_SEPERATOR);
		this.titleLabel = filterSepStr[0];
		LoggingMessages.printOut(titleLabel);
		this.domain = filterSepStr[1];
		LoggingMessages.printOut(domain);
		
		for(int i = 2; i < filterSepStr.length-1; i++)
		{
			String parseType = filterSepStr[i];
			LinkedHashMap<String, ArrayList<String[]>> filter = new LinkedHashMap<String, ArrayList<String[]>>();
			i++;
			
			for(int j = 1; j < filterSepStr[i].split(DELIMITER_MATCH_FILTER_SEPERATOR).length; j++)
			{
				String [] listFull = filterSepStr[i].split(DELIMITER_MATCH_FILTER_SEPERATOR)[j].split(DELIMITER_REPLACE_SEPERATOR);
				String match = listFull[0];
				String [] replaces = new String [0];
				if(j < filterSepStr[i].split(DELIMITER_MATCH_FILTER_SEPERATOR).length)
				{
					replaces = new String[listFull.length-1];
					for(int k = 1; k < listFull.length; k++)
						replaces[k-1] = listFull[k];
				}
				
				LoggingMessages.printOut(" parseType: " + parseType +
						" | match value: " + match +
						" | replaces length: " + replaces.length);
				filter.put(match, new ArrayList<String[]>());
				
				filter = getMatchReplace(filterSepStr[i], filter, match, replaces);
			}
			
			ParseAttribute pa = new ParseAttribute(parseType);
			pas.addAttribute(pa);
			pageMatchAndReplace.put(pa, filter);
		}
	}
	
	private LinkedHashMap<String, ArrayList<String[]>> getMatchReplace(
			String filterStripped, LinkedHashMap<String, ArrayList<String[]>> filter, String match, String [] replaces)
	{
		if(replaces.length == 0)
		{
			filter.put(match, new ArrayList<String[]>());
		}
		for(int j = 0; j < replaces.length; j++)
		{
			String [] repls = replaces[j].split(DELIMITER_REPLACE_VALUE_SEPERATOR);
			if(repls.length == 1)
			{
				repls = new String [] {repls[0],""};
			}
			else
			{
				ArrayList<String> copy = new ArrayList<String>();
				for(String r : repls)
				{
					if(r.contains(DELIMITER_LIST_FILTER_SEPERATOR))
					{
						r = r.split(DELIMITER_LIST_FILTER_SEPERATOR)[0];
					}
					copy.add(r);
				}
				repls = copy.toArray(new String[] {});
			}
			if(!filter.containsKey(match))
			{
				filter.put(match, new ArrayList<String[]>());
			}
			filter.get(match).add(repls);
		}
		
		return filter;
	}
	
	public void setXmlString(String xmlString)
	{
		readXmlString(xmlString);
	}
	
	private String [] stripToMatch(String response, String pat, boolean singleMatch)
	{
		Pattern pattern = Pattern.compile(pat);
		Matcher m = pattern.matcher(response);
		if(singleMatch)
		{
			if(m.find())
			{
				return new String [] {m.group()};
			}
			else
			{
				return new String []{""};
			}
		}
		else
		{
			ArrayList<String> retMatches = new ArrayList<String>();
			while(m.find())
			{
				String s = m.group();
				retMatches.add(s);
			}
			return retMatches.toArray(new String[retMatches.size()]);
		}
	}
	
	private String [] replaceStrip(ArrayList<String[]> replaces, String ... attributes)
	{
		String [] retAttributes = new String [attributes.length];
		int count = 0;
		for(String att : attributes)
		{
			for(String [] repl : replaces)
			{
				att = att.replaceAll(repl[0], repl[1]);
			}
			LoggingMessages.printOut(att);
			retAttributes[count] = att;
			count++;
		}
		return retAttributes;
	}
	
	public static void main(String [] args)
	{
		PageParser youtube = new PageParser("Youtube@F@youtube.com@F@Image@F@@M@https://yt3.googleusercontent.com([^@Q@])*(@Q@)@R@@Q@@RV@@F@Title@F@@M@<title>([^<])*</title>@R@<title>@RV@@R@</title>@RV@@R@[^a-zA-Z0-9\\-\\s]@RV@");
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
