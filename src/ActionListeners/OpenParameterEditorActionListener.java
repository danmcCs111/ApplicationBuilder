package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import ApplicationBuilder.BuilderWindow;
import ApplicationBuilder.DependentRedrawableFrame;
import ApplicationBuilder.LoggingMessages;
import WidgetExtensions.ApplicationLayoutEditor;
import WidgetExtensions.XmlToEditor;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class OpenParameterEditorActionListener implements ActionListener 
{
	private BuilderWindow builderWindow;
	private DependentRedrawableFrame parentEditor;
	private XmlToEditor xe;
	
	public OpenParameterEditorActionListener(XmlToEditor xe, DependentRedrawableFrame parentEditor)
	{
		this.parentEditor = parentEditor;
		this.xe = xe;
	}
	
	public BuilderWindow getBuilderWindow()
	{
		return this.builderWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		List<WidgetCreatorProperty> wcps = WidgetBuildController.getInstance().getWidgetCreatorProperties();
		if(wcps == null || wcps.isEmpty() || xe == null || xe.getSelectedIndex()==-1)
			return;
		
		WidgetCreatorProperty wcp = wcps.get(xe.getSelectedIndex());
		String sel = xe.getTitleAt(xe.getSelectedIndex());
		LoggingMessages.printOut(sel);
		
		if(builderWindow == null )
		{
			builderWindow = new BuilderWindow(parentEditor, wcp);
		}
		
		builderWindow.setComboSelection(sel);
		builderWindow.setWidgetCreatorProperty(wcp);
		builderWindow.setVisible(true);
	}

}
