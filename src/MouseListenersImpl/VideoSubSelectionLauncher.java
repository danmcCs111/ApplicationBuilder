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
import WidgetComponentInterfaces.HighlightListener;
import WidgetComponents.JButtonLengthLimited;

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
	
	public VideoSubSelectionLauncher(AbstractButton component)
	{
		this.parentComponent = component;
	}
	
	public VideoSubSelectionLauncher(AbstractButton component, AbstractButton childButton, HighlightListener hlListener)
	{
		this(component, childButton, hlListener, -1);
	}
	
	public VideoSubSelectionLauncher(AbstractButton component, AbstractButton childButton, HighlightListener hlListener, int id)
	{
		this.parentComponent = component;
		this.childComponent = childButton;
		this.hlListener = hlListener;
		this.id = id;
	}
	
	public static void setPortNumber(int portNumber)
	{
		VideoSubSelectionLauncher.portNumber = portNumber;
	}
	
	public void performLaunch(Component childButton, int id)
	{
		String [] args = LaunchUrlActionListener.buildCommand(childButton, id);
		LaunchUrlActionListener.executeProcess(id, args);
		
		VideoSubSelectionLauncher.launchRequest((JButtonLengthLimited) childComponent, id);
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
		
		VideoSubSelectionLauncher.launchRequest((JButtonLengthLimited) childComponent, -1);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(id == -1)
		{
			performLaunch(this.childComponent);
			launchRequest((JButtonLengthLimited) this.childComponent, -1);
		}
		else
		{
			performLaunch(childComponent, id);
			launchRequest((JButtonLengthLimited) this.childComponent, 1);
		}
	}
	
	public static void launchRequest(JButtonLengthLimited jbll, int id)
	{
		String req = 
				jbll.getText() + HttpLaunchUrlRequest.ARG_DELIMITER + 
				jbll.getFullLengthText() + HttpLaunchUrlRequest.ARG_DELIMITER +
				jbll.getHighlightButton().getText() + HttpLaunchUrlRequest.ARG_DELIMITER +
				((JButtonLengthLimited) jbll.getHighlightButton()).getFullLengthText() + HttpLaunchUrlRequest.ARG_DELIMITER + 
				jbll.getName() + HttpLaunchUrlRequest.ARG_DELIMITER +
				id;
		
		HttpDatabaseRequest.executeGetRequest(
				QueryUpdateTool.ENDPOINT,
				portNumber,
				req,
				HttpRequestHandler.REQUEST_TYPE_HEADER_KEY,
				HttpRequestHandler.FUNCTION_TYPE_LAUNCH_URL
		);
	}
	
}
