package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.TabbedPanel;
import WidgetUtility.WidgetBuildController;

public class RemoveEditorTabActionListener implements ActionListener 
{
	private TabbedPanel xmlToEditor;
	
	public RemoveEditorTabActionListener(TabbedPanel xmlToEditor)
	{
		this.xmlToEditor = xmlToEditor;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		int index = xmlToEditor.getSelectedIndex();
		WidgetBuildController.getInstance().getWidgetCreatorProperties().remove(index);
		xmlToEditor.rebuildPanel();
	}
}
