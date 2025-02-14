package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.DependentRedrawableFrame;
import ApplicationBuilder.DependentRedrawableFrameListener;
import ApplicationBuilder.EditorToXml;
import WidgetUtility.WidgetBuildController;

public class SaveEditorActionListener implements DependentRedrawableFrameListener, ActionListener
{
	private DependentRedrawableFrame applicationLayoutEditor;
	
	public void setDependentRedrawableFrame(DependentRedrawableFrame applicationLayoutEditor)
	{
		this.applicationLayoutEditor = applicationLayoutEditor;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		EditorToXml.writeXml(WidgetBuildController.getInstance().getFilename(),
				WidgetBuildController.getInstance().getWidgetCreatorProperties());
		
//		WidgetBuildController.getInstance().readProperties(chosenFile);
		applicationLayoutEditor.rebuildInnerPanels();
	}
}
