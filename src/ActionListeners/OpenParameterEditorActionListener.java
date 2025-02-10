package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.BuilderWindow;
import ApplicationBuilder.LoggingMessages;
import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;
import WidgetUtility.XmlToEditor;

public class OpenParameterEditorActionListener implements ActionListener 
{
	private BuilderWindow builderWindow; 
	private XmlToEditor xe;
	
	public OpenParameterEditorActionListener(XmlToEditor xe)
	{
		this.xe = xe;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		WidgetCreatorProperty wcp = WidgetBuildController.getWidgetCreationProperties().get(xe.getSelectedIndex());
		String sel = xe.getTabbedPane().getTitleAt(xe.getTabbedPane().getSelectedIndex());
		LoggingMessages.printOut(sel);
		
		if(builderWindow == null )
		{
			builderWindow = new BuilderWindow();
		}
		builderWindow.setComboSelection(sel);
		builderWindow.setWidgetCreatorProperty(wcp);
		builderWindow.setVisible(true);
	}

}
