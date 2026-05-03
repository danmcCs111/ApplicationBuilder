package HttpDatabaseRequest;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import ApplicationBuilder.QueryUpdateTool;
import HttpDatabaseRequest.HttpRequestHandler.ProcessType;
import MouseListenersImpl.PicLabelMouseListener;
import MouseListenersImpl.VideoSubSelectionLauncher;
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
	
	public HttpLaunchUrlRequest()
	{
		addArrayActionListener();
		virtualButton.setHighlightButton(virtualButtonHighlight);
		virtualButton.addActionListener(new LaunchUrlActionListener());
	}
	
	public void processLaunch(String responseXml, ProcessType proc, ArrayActionListener ...aals)
	{
		String [] args = responseXml.split(ARG_DELIMITER);
		if(args.length <= 1)//do close
		{
			switch(proc)
			{
			case ProcessType.parent:
				virtualButton.setName(LaunchUrlActionListener.CLOSE_LAUNCH_ACTION_EVENT);
				virtualButton.doClick();
				PicLabelMouseListener.highLightLabel(virtualButtonHighlight, false);
				return;
			case ProcessType.child:
				for(ArrayActionListener aal : aals)
				{
					aal.urlSelect(null);
				}
				return;
			}
		}
		
		String
			sourceButton = args[0],
			sourceButtonFull = args[1],
			highlightButton = args[2],
			highlightButtonFull = args[3],
			url = args[4],
			idStr = args[5],
			portStr = args[6];
	
		int 
			id = Integer.parseInt(idStr),
			port = Integer.parseInt(portStr);
		
		if(!portNumbers.contains(port))
		{
			portNumbers.add(port);
			VideoSubSelectionLauncher.setPortNumber(port);
		}
		
		//Referenced -> FileListOptionGenerator
		virtualButton.setText(sourceButton);
		virtualButton.setFullText(sourceButtonFull);
		virtualButton.setName(url);
		virtualButtonHighlight.setText(highlightButton);
		virtualButtonHighlight.setFullText(highlightButtonFull);
		
		if(id == -1)
		{
			switch(proc)
			{
			case ProcessType.parent:
				virtualButton.doClick();
				PicLabelMouseListener.highLightLabel(virtualButtonHighlight, true);
				return;
			case ProcessType.child:
				for(ArrayActionListener aal : aals)
				{
					aal.urlSelect(virtualButton);
				}
				return;
			}
		}
		else if(proc.equals(ProcessType.parent))
		{
			String [] argsP = LaunchUrlActionListener.buildCommand(virtualButton, id);
			LaunchUrlActionListener.executeProcess(id, argsP);
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
