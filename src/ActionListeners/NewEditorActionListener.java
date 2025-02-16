package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.DependentRedrawableFrame;
import ApplicationBuilder.DependentRedrawableFrameListener;
import WidgetUtility.WidgetBuildController;

public class NewEditorActionListener implements DependentRedrawableFrameListener, ActionListener
{
	private DependentRedrawableFrame applicationLayoutEditor;
	
	public void setDependentRedrawableFrame(DependentRedrawableFrame applicationLayoutEditor)
	{
		this.applicationLayoutEditor = applicationLayoutEditor;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(WidgetBuildController.getInstance().getFilename() != null)
		{
			WidgetBuildController.getInstance().destroy();
			WidgetBuildController.getInstance().clearFilename();
			
			applicationLayoutEditor.rebuildInnerPanels();
		}
	}

}
