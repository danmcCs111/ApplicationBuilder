package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import WidgetUtility.WidgetBuildController;

public class GenerateActionListener implements ActionListener 
{
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(WidgetBuildController.getWidgetCreatorProperties() != null)
		{
			WidgetBuildController.generateGraphicalInterface();
		}	
	}

}
