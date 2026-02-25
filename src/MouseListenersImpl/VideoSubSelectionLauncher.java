package MouseListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import ActionListenersImpl.LaunchUrlActionListener;
import WidgetComponentInterfaces.HighlightListener;

public class VideoSubSelectionLauncher implements ActionListener
{
	private AbstractButton 
		component,
		childButton;
	private HighlightListener 
		hlListener;
	
	public VideoSubSelectionLauncher(AbstractButton component)
	{
		this.component = component;
	}
	
	public VideoSubSelectionLauncher(AbstractButton component, AbstractButton childButton, HighlightListener hlListener)
	{
		this.component = component;
		this.childButton = childButton;
		this.hlListener = hlListener;
	}
	
	public void performLaunch(AbstractButton childButton, int id)
	{
		String [] args = LaunchUrlActionListener.buildCommand(childButton, id);
		LaunchUrlActionListener.executeProcess(id, args);
	}
	
	public void performLaunch(AbstractButton childButton) //double loop b/c of order.
	{
		for(ActionListener al : component.getActionListeners())
		{
			if(al instanceof LaunchUrlActionListener)
			{
				Object source = childButton;
				if(hlListener != null)
				{
					AbstractButton ab = hlListener.getMatchingButton(childButton.getName());
					if(ab != null)
					{
						source = ab;
					}
				}
				al.actionPerformed(new ActionEvent(source, 1, "Open From Image"));
			}
		}
		for(ActionListener al : component.getActionListeners())
		{
			if(!(al instanceof LaunchUrlActionListener))
			{
				al.actionPerformed(new ActionEvent(component, 1, "Open From Image"));
				PicLabelMouseListener.highLightLabel(component, true);//TODO
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
		performLaunch(this.childButton);
	}
}
