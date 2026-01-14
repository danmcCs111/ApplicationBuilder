package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import WidgetComponentInterfaces.RedrawableFrame;
import WidgetComponentInterfaces.RedrawableFrameListener;

public class ComponentComboBoxActionListener implements RedrawableFrameListener, ActionListener 
{
	private RedrawableFrame redrawFrame;
	
	public void setRedrawableFrame(RedrawableFrame redrawFrame)
	{
		this.redrawFrame = redrawFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		redrawFrame.rebuildInnerPanels();
	}

}
