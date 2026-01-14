package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import WidgetComponentInterfaces.RedrawableFrame;
import WidgetComponentInterfaces.RedrawableFrameListener;
import WidgetUtility.WidgetBuildController;

public class NewEditorActionListener implements RedrawableFrameListener, ActionListener
{
	private RedrawableFrame applicationLayoutEditor;
	
	public void setRedrawableFrame(RedrawableFrame applicationLayoutEditor)
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
