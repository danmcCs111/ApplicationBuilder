package HttpDatabaseRequest;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import ApplicationBuilder.QueryUpdateTool;
import MouseListenersImpl.PicLabelMouseListener;
import MouseListenersImpl.VideoSubSelectionLauncher;
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
	private static ArrayList<Integer> 
		portNumbers = new ArrayList<Integer>();
	
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
		if(args.length <= 1)//do close
		{
			virtualButton.setName(LaunchUrlActionListener.CLOSE_LAUNCH_ACTION_EVENT);
			virtualButton.doClick();
			PicLabelMouseListener.highLightLabel(virtualButtonHighlight, false);
			return;
		}
		
		String
			sourceButton = args[0],
			sourceButtonFull = args[1],
			highlightButton = args[2],
			highlightButtonFull = args[3],
			url = args[4],
			idStr = args[5];
		
		int id = Integer.parseInt(idStr);
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
		
		LoggingMessages.printOut("add subscriber: " + responseXml);
		int port = Integer.parseInt(responseXml);
		if(!portNumbers.contains(port))
		{
			portNumbers.add(port);
			VideoSubSelectionLauncher.setPortNumber(port);
		}
	}
	
	private static void notifySubscribers(String req)
	{
		for(int port : portNumbers)
		{
			HttpDatabaseRequest.executeGetRequest(
				QueryUpdateTool.ENDPOINT,
				port,
				req,
				HttpRequestHandler.REQUEST_TYPE_HEADER_KEY,
				HttpRequestHandler.FUNCTION_TYPE_LAUNCH_URL
			);
		}
	}

	@Override
	public void addActionListener(ActionListener actionListener) 
	{
		
	}

	@Override
	public void urlSelect(AbstractButton newButton) 
	{
		JButtonLengthLimited jbll = (JButtonLengthLimited) newButton;
		String req = "CloseEvent";
		
		if(jbll != null)
		{
			req = 
				jbll.getText() + HttpLaunchUrlRequest.ARG_DELIMITER + 
				jbll.getFullLengthText() + HttpLaunchUrlRequest.ARG_DELIMITER +
				jbll.getHighlightButton().getText() + HttpLaunchUrlRequest.ARG_DELIMITER +
				((JButtonLengthLimited) jbll.getHighlightButton()).getFullLengthText() + HttpLaunchUrlRequest.ARG_DELIMITER + 
				jbll.getName() + HttpLaunchUrlRequest.ARG_DELIMITER +
				-1+"";
		}
		notifySubscribers(req);
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
				HttpRequestProcessor.getPortNumber(),
				"Free Documentary" + ARG_DELIMITER + "Free Documentary" + ARG_DELIMITER + 
				"Free Documentary" + ARG_DELIMITER + "Free Documentary" + ARG_DELIMITER + 
				"https://www.youtube.com/@FreeDocumentary" + ARG_DELIMITER + 
				-1 + "",
				HttpRequestHandler.REQUEST_TYPE_HEADER_KEY,
				HttpRequestHandler.FUNCTION_TYPE_LAUNCH_URL
		);
	}
}
