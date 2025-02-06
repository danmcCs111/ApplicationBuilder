package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.BuilderWindow;
import ApplicationBuilder.LoggingMessages;
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
		String sel = xe.getTabbedPane().getTitleAt(xe.getTabbedPane().getSelectedIndex());
		LoggingMessages.printOut(sel);
		
		if(builderWindow == null )
		{
			builderWindow = new BuilderWindow();
		}
		builderWindow.setComboSelection(sel);
		builderWindow.setVisible(true);
	}

}
