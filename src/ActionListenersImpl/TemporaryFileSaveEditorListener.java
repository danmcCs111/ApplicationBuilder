package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import WidgetComponents.EditorToXml;
import WidgetUtility.WidgetBuildController;

public class TemporaryFileSaveEditorListener implements ActionListener
{
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(WidgetBuildController.getInstance().getWidgetCreatorProperties().isEmpty())
		{
			return;
		}
			
		EditorToXml.writeXml(WidgetBuildController.getInstance().getTemporaryFile(),
				WidgetBuildController.getInstance().getWidgetCreatorProperties());
	}
}
