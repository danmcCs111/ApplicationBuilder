package HttpDatabaseRequest;

import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;

public class HttpLaunchUrlRequest implements ArrayActionListener
{
	private static String
		ARG_DELIMITER = "@@";
	private static HttpLaunchUrlRequest 
		self = new HttpLaunchUrlRequest();
	
	private HttpLaunchUrlRequest()
	{
		addArrayActionListener();
	}
	
	public static void processLaunch(String responseXml)
	{
		String [] args = responseXml.split(ARG_DELIMITER);
		String
			sourceButton = args[0],
			highlightButton = args[1],
			url = args[2];
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
	
}
