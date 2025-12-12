package ObjectTypeConversion;

import java.util.ArrayList;

public class PageParserCollection 
{
	private static String PARSER_DELIMIT = "@C@";
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
		String [] tmp = xmlString.split(PARSER_DELIMIT);
		for(String t : tmp)
		{
			PageParser pp = new PageParser(t);
			addPageParser(pp);
		}
	}
	
	public String getXmlString()
	{
		String xmlStr = "";
		for(PageParser pp : pageParsers)
		{
			xmlStr += pp.getXmlString() + PARSER_DELIMIT;
		}
		xmlStr = xmlStr.replaceAll("(" + PARSER_DELIMIT + ")$", "");
		
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
