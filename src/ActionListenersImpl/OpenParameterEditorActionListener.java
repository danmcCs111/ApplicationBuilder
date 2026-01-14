package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import Properties.LoggingMessages;
import WidgetComponentInterfaces.RedrawableFrame;
import WidgetComponentInterfaces.RedrawableFrameListener;
import WidgetComponentInterfaces.WidgetBuildIndexSelector;
import WidgetComponents.ApplicationLayoutEditor;
import WidgetComponents.BuilderWindow;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class OpenParameterEditorActionListener implements RedrawableFrameListener, ActionListener 
{
	private BuilderWindow builderWindow;
	private RedrawableFrame redrawableFrame;
	private WidgetBuildIndexSelector xe;
	
	public BuilderWindow getBuilderWindow()
	{
		return this.builderWindow;
	}
	
	@Override
	public void setRedrawableFrame(RedrawableFrame redrawableFrame) 
	{
		this.redrawableFrame = redrawableFrame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) //TODO
	{
		List<WidgetCreatorProperty> wcps = WidgetBuildController.getInstance().getWidgetCreatorProperties();
		if(redrawableFrame instanceof ApplicationLayoutEditor)
		{
			xe = ((ApplicationLayoutEditor) redrawableFrame).getXmlToEditor();
		}
		if(wcps == null || wcps.isEmpty() || xe == null || xe.getSelectedIndex()==-1)
			return;
		
		WidgetCreatorProperty wcp = wcps.get(xe.getSelectedIndex());
		String sel = xe.getTitleAt(xe.getSelectedIndex());
		LoggingMessages.printOut(sel);
		
		if(builderWindow != null)//1 for now
		{
			builderWindow.dispose();
		}
		
		builderWindow = new BuilderWindow(redrawableFrame, wcp);
		((ApplicationLayoutEditor) redrawableFrame).setBuilderWindow(builderWindow);
		builderWindow.setComboSelection(sel);
		builderWindow.setWidgetCreatorProperty(wcp);
		
		builderWindow.setVisible(true);
	}

}
