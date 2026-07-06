package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

import HttpDatabaseRequest.HttpDatabaseRequest;
import ObjectTypeConversion.PageParser;
import ObjectTypeConversion.PageParserCollection;
import ObjectTypeConversion.ParseAttribute;
import Properties.LoggingMessages;

public class PageParserSequenceLaunch implements ActionListener 
{
	private PageParserCollection 
		pageParserCollection;
	private String 
		homepage;
	private LinkedHashMap<ParseAttribute, String[]> 
		parsePagesAndMatches = new LinkedHashMap<ParseAttribute, String[]>();
	private int index = 0;
	
	public PageParserSequenceLaunch(PageParserCollection pageParserCollection, String homepage) 
	{
		this.pageParserCollection = pageParserCollection;
		this.homepage = homepage;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String htmlResponse = HttpDatabaseRequest.executeGetRequest(homepage);
		index = 0;
		simulateAction(htmlResponse, pageParserCollection.getPageParsers().get(index));
		for(ParseAttribute pa : parsePagesAndMatches.keySet())
		{
			String [] matches = parsePagesAndMatches.get(pa);
			LoggingMessages.printOut(pa.name());
			LoggingMessages.printOut(LoggingMessages.combine(matches));
		}
	}
	
	private void simulateAction(String htmlResponse, PageParser pageParser)
	{
		int len = 0;
		for(ParseAttribute pa : pageParser.getParseAttributes())
		{
			String [] matches = pageParser.getAttributesFromResponse(pa, htmlResponse, false);
			if(matches == null || matches.length == 0)
				continue;
			
			parsePagesAndMatches.put(pa, matches);
			LoggingMessages.printOut(pa.name());
			LoggingMessages.printOut(LoggingMessages.combine(matches));
			if(pa.name().toLowerCase().contains("link"))
			{
				for(String m : matches)
				{
					if(m.strip().isBlank())
						continue;
					
					try{
						String htmlResp = HttpDatabaseRequest.executeGetRequest(m);
						if(index + 1 < pageParserCollection.getPageParsers().size()-1)
							index++;
						simulateAction(htmlResp, pageParserCollection.getPageParsers().get(1));
					}catch(Exception e)
					{
						LoggingMessages.printOut("failed request: " + m);
					}
				}
			}
			
			if(matches.length < len || len == 0)
				len = matches.length;//TODO
		}
	}

}
