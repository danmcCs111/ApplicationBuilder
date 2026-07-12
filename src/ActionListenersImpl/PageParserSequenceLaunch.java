package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

import HttpDatabaseRequest.HttpDatabaseRequest;
import ObjectTypeConversion.PageParser;
import ObjectTypeConversion.PageParserCollection;
import ObjectTypeConversion.ParseAttribute;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.TextOutputSubscriber;

public class PageParserSequenceLaunch implements ActionListener 
{
	private static String
		SEQUENCE_SPLIT_TAG = "link";
	private PageParserCollection 
		pageParserCollection;
	private String
		homepage;
	private LinkedHashMap<ParseAttribute, String[]> 
		parsePagesAndMatches = new LinkedHashMap<ParseAttribute, String[]>();
	private TextOutputSubscriber 
		textOutputSubscriber;
	private Thread
		runThread;
	private long
		sleepMillis;
	
	public PageParserSequenceLaunch(
			PageParserCollection pageParserCollection, 
			String homepage, 
			TextOutputSubscriber textSubscriber,
			long sleepMillis
	) 
	{
		this.pageParserCollection = pageParserCollection;
		this.homepage = homepage;
		this.textOutputSubscriber = textSubscriber;
		this.sleepMillis = sleepMillis;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(runThread == null || !runThread.isAlive())
		{
			String htmlResponse = HttpDatabaseRequest.executeGetRequest(homepage);
			Runnable r = new Runnable() {
				@Override
				public void run() {
					simulateAction(htmlResponse, 0);
				}
			};
			runThread = new Thread(r);
			runThread.start();
		}
	}
	
	private void simulateAction(String htmlResponse, int index)
	{
		if(index >= pageParserCollection.getPageParsers().size())
			index--;
		
		PageParser pageParser = pageParserCollection.getPageParsers().get(index);
		for(ParseAttribute pa : pageParser.getParseAttributes())
		{
			String [] matches = pageParser.getAttributesFromResponse(pa, htmlResponse, false);
			if(matches == null || matches.length == 0)
				continue;
			
			parsePagesAndMatches.put(pa, matches);
			
			textOutputSubscriber.textOutput(pa.name() + "\n" + LoggingMessages.combine(matches) + "\n");
			
			if(pa.name().toLowerCase().contains(SEQUENCE_SPLIT_TAG))
			{
				for(String m : matches)
				{
					if(m.strip().isBlank())
						continue;
					
					try {
						Thread.sleep(sleepMillis);
						String htmlResp = HttpDatabaseRequest.executeGetRequest(m);
						simulateAction(htmlResp, index+1);
					} catch(InterruptedException ie) {
						LoggingMessages.printOut("failed request: " + m);
					}
				}
			}
		}
	}

}
