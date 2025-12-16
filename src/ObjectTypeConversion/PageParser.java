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
	
	public static final String
		PARSER_DELIMIT_COLLECTION = "@C@",
		QUOTE_REPLACEMENT = "@Q@",
		DELIMITER_FILTER_SEPERATOR = "@F@",
		DELIMITER_REPLACE_SEPERATOR = "@R@",
		DELIMITER_REPLACE_VALUE_SEPERATOR = "@RV@",
		DELIMITER_LIST_FILTER_SEPERATOR = "@L@";
			
	private String 
		titleLabel = "",
		domain = "";
	private HashMap<ParseAttribute, LinkedHashMap<String, ArrayList<String[]>>>
		pageMatchAndReplace = new HashMap<ParseAttribute, LinkedHashMap<String, ArrayList<String[]>>>();
	
	public PageParser(String xmlString)
	{
		readXmlString(xmlString);
	}
	
	public void setDomainMatch(String domain)
	{
		this.domain = domain;
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
		String [] attributes = null;
		for(String key : pageMatchAndReplace.get(pa).keySet()) 
		{
			String [] tmp = stripToMatch(response, key, singleMatch);
			attributes = replaceStrip(tmp, pageMatchAndReplace.get(pa).get(key));
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
				retStr += mat + DELIMITER_FILTER_SEPERATOR;
				for(String [] repl : pageMatchAndReplace.get(pa).get(mat))
				{
					retStr += repl[0] + DELIMITER_REPLACE_VALUE_SEPERATOR + repl[1] + DELIMITER_REPLACE_SEPERATOR;
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
	
	/**
	 * title,domain,parsetype,[matches&replaces]
	 * @param xmlString
	 */
	private void readXmlString(String xmlString)//TODO need json
	{
		if(xmlString == null || xmlString.isBlank())
			return;
		
		xmlString = xmlString.replaceAll(QUOTE_REPLACEMENT, "\"");
		
		String [] filterSepStr = xmlString.split(DELIMITER_FILTER_SEPERATOR);
		this.titleLabel = filterSepStr[0];
		LoggingMessages.printOut(titleLabel);
		this.domain = filterSepStr[1];
		LoggingMessages.printOut(domain);
		
		for(int i = 2; i < filterSepStr.length-1; i++)
		{
			String parseType = filterSepStr[i];
			String match = filterSepStr[i+1];
			LinkedHashMap<String, ArrayList<String[]>> filter = new LinkedHashMap<String, ArrayList<String[]>>();
			i+=2;
			
			String [] replaces = filterSepStr[i].split(DELIMITER_REPLACE_SEPERATOR);//filterSepStr[i+1].split(DELIMITER_REPLACE_SEPERATOR);
			
			filter.put(match, new ArrayList<String[]>());
			LoggingMessages.printOut(replaces.length + " parseType: " + parseType + " match value: " + match);
			if(replaces.length > 1)
			{
				for(int j = 0; j < replaces.length; j++)
				{
					String [] repls = replaces[j].split(DELIMITER_REPLACE_VALUE_SEPERATOR);
					if(repls.length == 1)
					{
						repls = new String [] {repls[0],""};
					}
					filter.get(match).add(repls);
				}
//				i++;
			}
			else
			{
				String [] repls = filterSepStr[i].split(DELIMITER_REPLACE_VALUE_SEPERATOR);
				if(repls.length == 1)
				{
					repls = new String [] {repls[0],""};
				}
				filter.get(match).add(repls);
//					i++;
			}
			LoggingMessages.printOut(parseType);
			pageMatchAndReplace.put(ParseAttribute.valueOf(parseType), filter);
		}
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
	
	private String [] replaceStrip(String [] attributes, ArrayList<String[]> replaces)
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
		PageParser youtube = new PageParser("Youtube@F@youtube.com@F@Image@F@https://yt3.googleusercontent.com([^@Q@])*(@Q@)@F@@Q@@RV@@F@Title@F@<title>([^<])*</title>@F@<title>@RV@@R@</title>@RV@ boo@R@[^a-zA-Z0-9\\-\\s]@RV@");
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
