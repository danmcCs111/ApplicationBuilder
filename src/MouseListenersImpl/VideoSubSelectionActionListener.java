package MouseListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import ActionListenersImpl.LaunchUrlActionListener;

public class VideoSubSelectionActionListener implements ActionListener
{
	private AbstractButton 
		component,
		childButton;
	
	public VideoSubSelectionActionListener(AbstractButton component, AbstractButton childButton)
	{
		this.component = component;
		this.childButton = childButton;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		for(ActionListener al : component.getActionListeners())
		{
			if(al instanceof LaunchUrlActionListener)
			{
				al.actionPerformed(new ActionEvent(childButton, 1, "Open From Image"));
			}
			else
			{
				al.actionPerformed(new ActionEvent(component, 1, "Open From Image"));
				PicLabelMouseListener.highLightLabel(component, true);//TODO
			}
		}
	}
}
