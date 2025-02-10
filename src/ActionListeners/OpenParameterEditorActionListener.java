package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.ApplicationLayoutEditor;
import ApplicationBuilder.BuilderWindow;
import ApplicationBuilder.LoggingMessages;
import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;
import WidgetUtility.XmlToEditor;

public class OpenParameterEditorActionListener implements ActionListener 
{
	private BuilderWindow builderWindow;
	private ApplicationLayoutEditor parentEditor;
	private XmlToEditor xe;
	
	public OpenParameterEditorActionListener(XmlToEditor xe, ApplicationLayoutEditor parentEditor)
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
		WidgetCreatorProperty wcp = WidgetBuildController.getWidgetCreationProperties().get(xe.getSelectedIndex());
		String sel = xe.getTabbedPane().getTitleAt(xe.getTabbedPane().getSelectedIndex());
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
