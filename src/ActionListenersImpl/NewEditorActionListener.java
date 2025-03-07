package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import WidgetComponentInterfaces.DependentRedrawableFrame;
import WidgetComponentInterfaces.DependentRedrawableFrameListener;
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
			WidgetBuildController.getInstance().destroyEditors();
			WidgetBuildController.getInstance().destroyFrameAndCreatorProperties();
			WidgetBuildController.getInstance().clearFilename();
			
			applicationLayoutEditor.rebuildInnerPanels();
		}
	}

}
