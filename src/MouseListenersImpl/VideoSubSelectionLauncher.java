package MouseListenersImpl;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import ActionListenersImpl.LaunchUrlActionListener;
import ApplicationBuilder.QueryUpdateTool;
import HttpDatabaseRequest.HttpDatabaseRequest;
import HttpDatabaseRequest.HttpLaunchUrlRequest;
import HttpDatabaseRequest.HttpRequestHandler;
import HttpDatabaseRequest.HttpRequestHandler.ProcessType;
import HttpDatabaseRequest.HttpRequestProcessor;
import WidgetComponentInterfaces.HighlightListener;
import WidgetComponents.JButtonLengthLimited;
import WidgetComponents.JMenuLaunchUrl;

public class VideoSubSelectionLauncher implements ActionListener
{
	private static int 
		portNumber = -1;
	
	private AbstractButton 
		parentComponent;
	private Component
		childComponent;
	private HighlightListener 
		hlListener;
	private int
		id = -1;
	private ProcessType
		procType;
	
	public VideoSubSelectionLauncher(AbstractButton component, ProcessType procType)
	{
		this.parentComponent = component;
		this.procType = procType;
	}
	
	public VideoSubSelectionLauncher(AbstractButton component, AbstractButton childButton, HighlightListener hlListener, ProcessType procType)
	{
		this(component, childButton, hlListener, procType, -1);
	}
	
	public VideoSubSelectionLauncher(AbstractButton component, AbstractButton childButton, HighlightListener hlListener, ProcessType procType, int id)
	{
		this.parentComponent = component;
		this.childComponent = childButton;
		this.hlListener = hlListener;
		this.id = id;
		this.procType = procType;
	}
	
	public static void setPortNumber(int portNumber)
	{
		VideoSubSelectionLauncher.portNumber = portNumber;
	}
	
	public void performLaunch(Component childButton, int id)
	{
		if(procType == ProcessType.parent) 
		{
			String [] args = LaunchUrlActionListener.buildCommand(childButton, id);
			LaunchUrlActionListener.executeProcess(id, args);
		}
		
		VideoSubSelectionLauncher.launchRequest(childButton, id);
	}
	
	public void performLaunch(Component childComponent) //double loop b/c of order.
	{
		for(ActionListener al : parentComponent.getActionListeners())
		{
			if(al instanceof LaunchUrlActionListener)
			{
				Object source = childComponent;
				if(hlListener != null)
				{
					Component ab = hlListener.getMatchingComponent(childComponent.getName());
					if(ab != null)
					{
						source = ab;
					}
				}
				al.actionPerformed(new ActionEvent(source, 1, "Open From Image"));
			}
		}
		for(ActionListener al : parentComponent.getActionListeners())
		{
			if(!(al instanceof LaunchUrlActionListener))
			{
				al.actionPerformed(new ActionEvent(parentComponent, 1, "Open From Image"));
				PicLabelMouseListener.highLightLabel(parentComponent, true);//TODO
			}
		}
		if(hlListener != null)
		{
			hlListener.highlight();
		}
		
		VideoSubSelectionLauncher.launchRequest(childComponent, -1);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(id == -1)
		{
			performLaunch(this.childComponent);
		}
		else
		{
			performLaunch(childComponent, id);
		}
	}
	
	public static String getRequest(Component newButton, int id)
	{
		String req = "CloseEvent";
		if(newButton instanceof JButtonLengthLimited)
		{
			JButtonLengthLimited jbll = (JButtonLengthLimited) newButton;
			
			if(jbll != null)
			{
				req = 
					jbll.getText() + HttpLaunchUrlRequest.ARG_DELIMITER + 
					jbll.getFullLengthText() + HttpLaunchUrlRequest.ARG_DELIMITER +
					jbll.getHighlightButton().getText() + HttpLaunchUrlRequest.ARG_DELIMITER +
					((JButtonLengthLimited) jbll.getHighlightButton()).getFullLengthText() + HttpLaunchUrlRequest.ARG_DELIMITER + 
					jbll.getName() + HttpLaunchUrlRequest.ARG_DELIMITER +
					id+"" + HttpLaunchUrlRequest.ARG_DELIMITER + 
					HttpRequestProcessor.getPortNumber()+"";
			}
		}
		else if(newButton instanceof JMenuLaunchUrl)
		{
			JMenuLaunchUrl jmlu = (JMenuLaunchUrl) newButton;
			if(newButton != null)
			{
				req = 
					jmlu.getText() + HttpLaunchUrlRequest.ARG_DELIMITER + 
					jmlu.getText() + HttpLaunchUrlRequest.ARG_DELIMITER +
					jmlu.getHighlightButton().getText() + HttpLaunchUrlRequest.ARG_DELIMITER +
					((JButtonLengthLimited) jmlu.getHighlightButton()).getFullLengthText() + HttpLaunchUrlRequest.ARG_DELIMITER + 
					jmlu.getName() + HttpLaunchUrlRequest.ARG_DELIMITER +
					id+"" + HttpLaunchUrlRequest.ARG_DELIMITER + 
					HttpRequestProcessor.getPortNumber()+"";
			}
		}
		return req;
	}
	
	public static void launchRequest(Component jbll, int id)
	{
		if(portNumber == -1)
			return;
		
		String req = getRequest(jbll, id);
		
		HttpDatabaseRequest.executeGetRequest(
				QueryUpdateTool.ENDPOINT,
				portNumber,
				req,
				HttpRequestHandler.REQUEST_TYPE_HEADER_KEY,
				HttpRequestHandler.FUNCTION_TYPE_LAUNCH_URL
		);
		
		if(id == -1)
		{
			LaunchUrlActionListener.setLastButtonOrigin((AbstractButton) jbll);
		}
	}
	
}
