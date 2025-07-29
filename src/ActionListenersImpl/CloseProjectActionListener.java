package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import WidgetUtility.WidgetBuildController;

public class CloseProjectActionListener implements ActionListener 
{
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		WidgetBuildController.getInstance().destroyGeneratedFrame();
		
		WidgetBuildController.getInstance().destroyEditors();
		WidgetBuildController.getInstance().destroyFrame();
	}

}
