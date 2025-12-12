package ObjectTypeConversion;

import java.util.ArrayList;

public class PageParserCollection 
{
	private ArrayList<PageParser> 
		pageParsers = new ArrayList<PageParser>();
	
	public PageParserCollection(String xmlString)
	{
		
	}
	
	public void setPageParsers(ArrayList<PageParser> pageParsers)
	{
		this.pageParsers.clear();
		this.pageParsers = pageParsers;
	}
	
	public String getXmlString()
	{
		String xmlStr = "";
		for(PageParser pp : pageParsers)
		{
			xmlStr += pp.getXmlString();
		}
		return  xmlStr;
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
