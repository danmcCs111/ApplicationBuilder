package HttpDatabaseRequest;

import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import ApplicationBuilder.QueryUpdateTool;
import MouseListenersImpl.PicLabelMouseListener;
import Properties.LoggingMessages;
import WidgetComponents.JButtonLengthLimited;

public class HttpLaunchUrlRequest implements ArrayActionListener
{
	public static String
		ARG_DELIMITER = "-@-";
	private static HttpLaunchUrlRequest 
		self;
	private static JButtonLengthLimited
		virtualButton = new JButtonLengthLimited(),
		virtualButtonHighlight = new JButtonLengthLimited();
	
	static {
		self = new HttpLaunchUrlRequest();
	}
	
	private HttpLaunchUrlRequest()
	{
		addArrayActionListener();
		virtualButton.setHighlightButton(virtualButtonHighlight);
		virtualButton.addActionListener(new LaunchUrlActionListener());
	}
	
	public static void processLaunch(String responseXml)
	{
		String [] args = responseXml.split(ARG_DELIMITER);
		String
			sourceButton = args[0],
			sourceButtonFull = args[1],
			highlightButton = args[2],
			highlightButtonFull = args[3],
			url = args[4],
			idStr = args[5];
		
		int id = Integer.parseInt(idStr);
		LoggingMessages.printOut("perform launch: " + sourceButton);
		
		if(id == -1)
		{
			//Referenced -> FileListOptionGenerator
			virtualButton.setText(sourceButton);
			virtualButton.setFullText(sourceButtonFull);
			virtualButton.setName(url);
			virtualButtonHighlight.setText(highlightButton);
			virtualButtonHighlight.setFullText(highlightButtonFull);
			
			virtualButton.doClick();
			PicLabelMouseListener.highLightLabel(virtualButtonHighlight, true);
		}
		else
		{
			String [] argsP = LaunchUrlActionListener.buildCommand(virtualButton, id);
			LaunchUrlActionListener.executeProcess(id, argsP);
		}
	}
	
	public static void processAddSubscriber(String responseXml)
	{
		String [] args = responseXml.split(ARG_DELIMITER);
		String
			sourceButton = args[0],
			highlightButton = args[1],
			url = args[2];
	}
	
	private static void notifySubscribers()
	{
		
	}

	@Override
	public void addActionListener(ActionListener actionListener) 
	{
		
	}

	@Override
	public void urlSelect(AbstractButton newButton) 
	{
		
	}

	@Override
	public void addArrayActionListener() 
	{
		LaunchUrlActionListener.addArrayActionListener(this);
	}

	@Override
	public void removeArrayActionListener() 
	{
		LaunchUrlActionListener.removeArrayActionListener(this);
	}

	@Override
	public void addStripFilter(String filter) 
	{
		
	}
	
	
	public static void main(String [] args)
	{
		HttpDatabaseRequest.executeGetRequest(
				QueryUpdateTool.ENDPOINT,
				HttpRequestProcessor.portNumber,
				"Free Documentary" + ARG_DELIMITER + "Free Documentary" + ARG_DELIMITER + "https://www.youtube.com/@FreeDocumentary",
				HttpRequestHandler.REQUEST_TYPE_HEADER_KEY,
				HttpRequestHandler.FUNCTION_TYPE_LAUNCH_URL
		);
	}
}
