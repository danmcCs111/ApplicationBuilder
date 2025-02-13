package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import ApplicationBuilder.BuilderWindow;
import ApplicationBuilder.DependentRedrawableFrame;
import ApplicationBuilder.DependentRedrawableFrameListener;
import ApplicationBuilder.LoggingMessages;
import WidgetExtensions.ApplicationLayoutEditor2;
import WidgetExtensions.XmlToEditor;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class OpenParameterEditorActionListener2 implements DependentRedrawableFrameListener, ActionListener 
{
	private BuilderWindow builderWindow;
	private DependentRedrawableFrame dependentRedrawableFrame;
	private XmlToEditor xe;
	
	public BuilderWindow getBuilderWindow()
	{
		return this.builderWindow;
	}
	
	@Override
	public void setDependentRedrawableFrame(DependentRedrawableFrame dependentRedrawableFrame) 
	{
		this.dependentRedrawableFrame = dependentRedrawableFrame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) //TODO
	{
		List<WidgetCreatorProperty> wcps = WidgetBuildController.getInstance().getWidgetCreatorProperties();
		if(dependentRedrawableFrame instanceof ApplicationLayoutEditor2)
		{
			xe = ((ApplicationLayoutEditor2) dependentRedrawableFrame).getXmlToEditor();
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
		
		builderWindow = new BuilderWindow(dependentRedrawableFrame, wcp);
		((ApplicationLayoutEditor2) dependentRedrawableFrame).setBuilderWindow(builderWindow);
		builderWindow.setComboSelection(sel);
		builderWindow.setWidgetCreatorProperty(wcp);
		
		builderWindow.setVisible(true);
	}

}
