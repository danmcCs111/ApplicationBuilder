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
		if(WidgetBuildController.getGeneratedNum()==0)
		{
			WidgetBuildController.getInstance().destroyGeneratedFrame();
			WidgetBuildController.getInstance().clearWidgetCreatorProperties();
		}
		
		applicationLayoutEditor.rebuildInnerPanels();
	}

}
