package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import WidgetComponents.DependentRedrawableFrame;
import WidgetComponents.DependentRedrawableFrameListener;

public class ComponentComboBoxActionListener implements DependentRedrawableFrameListener, ActionListener 
{
	private DependentRedrawableFrame redrawFrame;
	
	public void setDependentRedrawableFrame(DependentRedrawableFrame redrawFrame)
	{
		this.redrawFrame = redrawFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		redrawFrame.rebuildInnerPanels();
	}

}
