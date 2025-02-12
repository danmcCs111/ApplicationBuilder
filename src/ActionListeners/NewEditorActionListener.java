package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.ApplicationLayoutEditor;
import ApplicationBuilder.WidgetBuildController;

public class NewEditorActionListener implements ActionListener 
{
	private ApplicationLayoutEditor applicationLayoutEditor;
	
	public NewEditorActionListener(ApplicationLayoutEditor applicationLayoutEditor)
	{
		this.applicationLayoutEditor = applicationLayoutEditor;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		WidgetBuildController.destroyGeneratedFrame();
		WidgetBuildController.clearWidgetCreatorProperties();
		applicationLayoutEditor.rebuildInnerPanels();
	}

}
