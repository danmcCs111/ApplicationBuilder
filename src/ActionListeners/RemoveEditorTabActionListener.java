package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import WidgetComponents.ComponentSelector;
import WidgetComponents.TabbedPanel;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

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
		WidgetCreatorProperty wcp = WidgetBuildController.getInstance().getWidgetCreatorProperties().get(index);
		List<String> componentsReassign = ComponentSelector.getComponentsFromParent(wcp.getRefWithID());
		ComponentSelector.setParentRefIdOnComponents(componentsReassign, wcp.getParentRefWithID());
		
		WidgetBuildController.getInstance().getWidgetCreatorProperties().remove(index);
		
		xmlToEditor.rebuildPanel();
	}
}
