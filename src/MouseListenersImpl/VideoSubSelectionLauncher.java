package MouseListenersImpl;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import ActionListenersImpl.LaunchUrlActionListener;
import WidgetComponentInterfaces.HighlightListener;

public class VideoSubSelectionLauncher implements ActionListener
{
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
	
	public void performLaunch(Component childButton, int id)
	{
		String [] args = LaunchUrlActionListener.buildCommand(childButton, id);
		LaunchUrlActionListener.executeProcess(id, args);
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
}
