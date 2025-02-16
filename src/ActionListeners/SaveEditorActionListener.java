package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.DependentRedrawableFrame;
import ApplicationBuilder.DependentRedrawableFrameListener;
import ApplicationBuilder.EditorToXml;
import ApplicationBuilder.LoggingMessages;
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
		if(WidgetBuildController.getInstance().getWidgetCreatorProperties().isEmpty())
		{
			return;
		}
		
		//TODO introduce "Save as" when no filename 
		if(WidgetBuildController.getInstance().getFilename() == null)
		{
			boolean saved = SaveAsEditorActionListener.performSaveAs(this.applicationLayoutEditor);
			LoggingMessages.printOut("Saved? " + saved);
		}
		else
		{
			EditorToXml.writeXml(WidgetBuildController.getInstance().getFilename(),
					WidgetBuildController.getInstance().getWidgetCreatorProperties());
			
			WidgetBuildController.getInstance().readProperties(WidgetBuildController.getInstance().getFilename());
			applicationLayoutEditor.rebuildInnerPanels();
		}
	}
}
