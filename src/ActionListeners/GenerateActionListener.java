package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.WidgetBuildController;

public class GenerateActionListener implements ActionListener 
{
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(WidgetBuildController.getWidgetCreationProperties() != null)
		{
			WidgetBuildController.generateGraphicalInterface(WidgetBuildController.getWidgetCreationProperties());
		}	
	}

}
