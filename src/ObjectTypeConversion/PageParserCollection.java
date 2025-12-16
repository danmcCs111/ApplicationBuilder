package ObjectTypeConversion;

import java.util.ArrayList;

import Properties.LoggingMessages;

public class PageParserCollection 
{
	private ArrayList<PageParser> 
		pageParsers = new ArrayList<PageParser>();
	
	public PageParserCollection(String xmlString)
	{
		readXmlString(xmlString);
	}
	
	public void setPageParsers(ArrayList<PageParser> pageParsers)
	{
		this.pageParsers.clear();
		this.pageParsers = pageParsers;
	}
	
	public void readXmlString(String xmlString)
	{
		String [] tmp = xmlString.split(PageParser.PARSER_DELIMIT_COLLECTION);
		for(String t : tmp)
		{
			LoggingMessages.printOut(t + "xml value");
			PageParser pp = new PageParser(t);
			addPageParser(pp);
		}
	}
	
	public String getXmlString()
	{
		String xmlStr = "";
		for(PageParser pp : pageParsers)
		{
			xmlStr += pp.getXmlString() + PageParser.PARSER_DELIMIT_COLLECTION;
		}
		xmlStr = xmlStr.replaceAll("(" + PageParser.PARSER_DELIMIT_COLLECTION + ")$", "");
		
		return xmlStr;
	}
	
	public void addPageParser(PageParser pp)
	{
		this.pageParsers.add(pp);
	}
	
	public ArrayList<PageParser> getPageParsers()
	{
		return this.pageParsers;
	}
	
}
